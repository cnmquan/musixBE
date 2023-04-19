package com.example.musixBE.services.social;

import com.example.musixBE.models.social.Comment;
import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.social.comment.CreateCommentRequest;
import com.example.musixBE.payloads.requests.social.comment.DeleteCommentRequest;
import com.example.musixBE.payloads.requests.social.comment.DeleteReplyRequest;
import com.example.musixBE.payloads.requests.social.comment.ModifyCommentRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.music.ListPlaylistBody;
import com.example.musixBE.payloads.responses.social.CommentBody;
import com.example.musixBE.payloads.responses.social.ListCommentBody;
import com.example.musixBE.repositories.CommentRepository;
import com.example.musixBE.repositories.PostRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.CommentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final CommentUtils commentUtils;
    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<CommentBody> getComment(String commentId) {
        final var comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(new CommentBody(musixMapper.commentToCommentDTO(comment.get())))
                .build();
    }

    public Response<CommentBody> createComment(CreateCommentRequest request, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        boolean isPostExisted = postRepository.findById(request.getPostId()).isPresent();
        if (!isPostExisted) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        User user = userRepository.findByUsername(username).get();
        Post post = postRepository.findById(request.getPostId()).get();
        Comment comment = Comment.builder()
                .ownerId(user.getId())
                .ownerUsername(user.getUsername())
                .replies(new ArrayList<>())
                .likedBy(new ArrayList<>())
                .dateCreated(System.currentTimeMillis())
                .lastModified(System.currentTimeMillis())
                .content(request.getContent())
                // is author should be set as true in the reply API
                .isAuthor(false)
                .build();
        commentRepository.save(comment);
        List<String> commentsId = post.getComments();
        commentsId.add(comment.getId());
        post.setComments(commentsId);
        postRepository.save(post);
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(CommentBody.builder().comment(musixMapper.commentToCommentDTO(comment)).build())
                .build();
    }

    public Response<CommentBody> likeOrDislikeComment(String commentId, String bearerToken) {
        final var username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserNameExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserNameExisted || username == null) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        final boolean isCommentExisted = commentRepository.findById(commentId).isPresent();
        if (!isCommentExisted) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        Comment comment = commentRepository.findById(commentId).get();
        User user = userRepository.findByUsername(username).get();
        List<String> likedBy = comment.getLikedBy();
        if (likedBy.contains(user.getId())) {
            likedBy.remove(user.getId());
        } else {
            likedBy.add(user.getId());
        }
        commentRepository.save(comment);
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(CommentBody.builder().comment(musixMapper.commentToCommentDTO(comment)).build())
                .build();
    }

    public Response<CommentBody> modifyComment(ModifyCommentRequest request, String bearerToken) {
        final var username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserNameExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserNameExisted || username == null) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        final var comment = commentRepository.findById(request.getCommentId());
        if (comment.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        if (!username.equals(comment.get().getOwnerUsername())) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                    .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                    .build();
        }
        Comment modifiedComment = comment.get();
        modifiedComment.setContent(request.getNewContent());
        modifiedComment.setLastModified(System.currentTimeMillis());
        commentRepository.save(modifiedComment);
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(CommentBody.builder().comment(musixMapper.commentToCommentDTO(modifiedComment)).build())
                .build();
    }

    public Response<CommentBody> reply(String commentId, CreateCommentRequest request, String bearerToken) {
        var comment = commentRepository.findById(commentId);

        String username = jwtService.extractUsername(bearerToken.substring(7));
        if (comment.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        Comment reply = Comment.builder()
                .ownerId(user.get().getId())
                .ownerUsername(username)
                .replies(new ArrayList<>())
                .likedBy(new ArrayList<>())
                .dateCreated(System.currentTimeMillis())
                .lastModified(System.currentTimeMillis())
                .content(request.getContent())
                .isAuthor(comment.get().getOwnerUsername().equals(username))
                .build();
        var savedReply = commentRepository.save(reply);
        var listReply = comment.get().getReplies();
        listReply.add(savedReply.getId());
        comment.get().setReplies(listReply);
        commentRepository.save(comment.get());
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .data(CommentBody.builder().comment(musixMapper.commentToCommentDTO(reply)).build())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    public Response<CommentBody> delete(DeleteCommentRequest request, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));

        if (username.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        var comment = commentRepository.findById(request.getCommentId());
        if (comment.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        if (!comment.get().getOwnerUsername().equals(username)) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                    .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                    .build();
        }
        var post = postRepository.findById(request.getPostId());
        if (post.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        if (!post.get().getComments().contains(request.getCommentId())) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFoundOnProvidedPost.getStatus())
                    .msg(StatusList.errorCommentNotFoundOnProvidedPost.getMsg())
                    .build();
        }

        List<String> comments = post.get().getComments();
        comments.remove(request.getCommentId());
        post.get().setComments(comments);
        postRepository.save(post.get());
        commentUtils.deleteComment(request.getCommentId());
        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }


    public Response<CommentBody> deleteReply(DeleteReplyRequest request, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));

        if (username.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        var reply = commentRepository.findById(request.getReplyId());
        if (reply.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        if (!reply.get().getOwnerUsername().equals(username)) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                    .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                    .build();
        }
        var comment = commentRepository.findById(request.getCommentId());
        if (comment.isEmpty()) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        if (!comment.get().getReplies().contains(request.getReplyId())) {
            return Response.<CommentBody>builder()
                    .status(StatusList.errorReplyNotFoundOnProvidedComment.getStatus())
                    .msg(StatusList.errorReplyNotFoundOnProvidedComment.getMsg())
                    .build();
        }

        comment.get().getReplies().remove(reply.get().getId());
        commentRepository.save(comment.get());
        commentUtils.deleteComment(request.getReplyId());

        return Response.<CommentBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    public Response<ListCommentBody> getCommentsByPost(String postId) {
        try {
            final var post = postRepository.findById(postId).orElseThrow(() -> new Exception(StatusList.errorPostNotFound.getMsg()));
            final var comments = new ArrayList<Comment>();
            for(var commentId : post.getComments()){
                final var comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception(StatusList.errorCommentNotFoundOnProvidedPost.getMsg()));
                comments.add(comment);
            }
            return Response.<ListCommentBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(new ListCommentBody(musixMapper.commentListToCommentDTOList(comments)))
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorPostNotFound.getMsg())) {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorPostNotFound.getStatus())
                        .msg(StatusList.errorPostNotFound.getMsg())
                        .build();
            } else if (e.getMessage().equals(StatusList.errorCommentNotFoundOnProvidedPost.getMsg())) {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorCommentNotFoundOnProvidedPost.getStatus())
                        .msg(StatusList.errorCommentNotFoundOnProvidedPost.getMsg())
                        .build();
            }else {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListCommentBody> getReplyCommentByComment(String commentId) {
        try {
            final var comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception(StatusList.errorCommentNotFound.getMsg()));
            final var replyComments = new ArrayList<Comment>();
            for(var replyId : comment.getReplies()){
                final var reply = commentRepository.findById(replyId).orElseThrow(() -> new Exception(StatusList.errorReplyNotFoundOnProvidedComment.getMsg()));
                replyComments.add(reply);
            }
            return Response.<ListCommentBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(new ListCommentBody(musixMapper.commentListToCommentDTOList(replyComments)))
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorCommentNotFound.getMsg())) {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorCommentNotFound.getStatus())
                        .msg(StatusList.errorCommentNotFound.getMsg())
                        .build();
            } else if (e.getMessage().equals(StatusList.errorReplyNotFoundOnProvidedComment.getMsg())) {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorReplyNotFoundOnProvidedComment.getStatus())
                        .msg(StatusList.errorReplyNotFoundOnProvidedComment.getMsg())
                        .build();
            }else {
                return Response.<ListCommentBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }
}

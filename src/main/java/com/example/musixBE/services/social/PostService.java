package com.example.musixBE.services.social;

import com.example.musixBE.models.social.Comment;
import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.social.comment.CreateCommentRequest;
import com.example.musixBE.payloads.requests.social.post.DeleteCommentRequest;
import com.example.musixBE.payloads.requests.social.post.PostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.ListPostBody;
import com.example.musixBE.payloads.responses.social.PostBody;
import com.example.musixBE.repositories.CommentRepository;
import com.example.musixBE.repositories.PostRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.CommentUtils;
import com.example.musixBE.utils.FileType;
import com.example.musixBE.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final FileUtils fileUtils;
    private final CommentUtils commentUtils;
    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<PostBody> getPostById(String postId) {
        var post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(new PostBody(musixMapper.postToPostDTO(post.get())))
                .build();

    }

    public Response<ListPostBody> getPostsByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Response.<ListPostBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        var posts = postRepository.findByUsername(user.get().getUsername());
        return Response.<ListPostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(new ListPostBody(musixMapper.listPostToListPostDTO(posts)))
                .build();
    }

    public Response<PostBody> createPost(PostRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            boolean isUserExisted = userRepository.findByUsername(username).isPresent();
            if (!isUserExisted || username == null) {
                return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
            }
            var cloudId = UUID.randomUUID();
            var sourceId = username + "/social/post/source/" + cloudId;
            User user = userRepository.findByUsername(username).get();
            var dataUrl = fileUtils.upload(request.getFile(), sourceId);

            Post post = Post.builder()
                    .ownerId(user.getId())
                    .ownerUsername(user.getUsername())
                    .fileId(sourceId)
                    .comments(new ArrayList<>())
                    .content(request.getContent())
                    .dateCreated(System.currentTimeMillis())
                    .lastModified(System.currentTimeMillis())
                    .likedBy(new ArrayList<>())
                    .fileUrl(dataUrl)
                    .build();
            if (request.getThumbnail() != null) {
                var thumbnailId = username + "/social/post/thumbnail/" + cloudId;
                var thumbnailUrl = fileUtils.upload(request.getThumbnail(), thumbnailId);
                post.setThumbnailId(thumbnailId);
                post.setThumbnailUrl(thumbnailUrl);
            }
            postRepository.save(post);
            return Response.<PostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).build();
        } catch (IOException e) {
            return Response.<PostBody>builder().status(400).msg(e.getMessage()).build();
        }
    }

    public Response<PostBody> modifyPost(PostRequest request, String postId, String bearerToken) {

        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            boolean isUserExisted = userRepository.findByUsername(username).isPresent();
            if (!isUserExisted || username == null) {
                return Response.<PostBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg()).build();
            }

            boolean isPostExisted = postRepository.findById(postId).isPresent();
            if (!isPostExisted) {
                return Response.<PostBody>builder()
                        .status(StatusList.errorPostNotFound.getStatus())
                        .msg(StatusList.errorPostNotFound.getMsg())
                        .build();
            }

            Post post = postRepository.findById(postId).get();
            if (!username.equals(post.getOwnerUsername())) {
                return Response.<PostBody>builder()
                        .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                        .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                        .build();
            }
            if (request.getContent() != null) {
                post.setContent(request.getContent());
            }
            var newCloudId = UUID.randomUUID();
            if (request.getFile() != null) {
                var newSourceId = username + "/social/post/source/" + newCloudId;
                var newDataUrl = fileUtils.upload(request.getFile(), newSourceId);

                fileUtils.destroy(post.getFileId(), FileType.video);
                post.setFileId(newSourceId);
                post.setFileUrl(newDataUrl);
            }
            if (request.getThumbnail() != null) {
                var newThumbnailId = username + "/social/post/thumbnail/" + newCloudId;
                var newThumbnailUrl = fileUtils.upload(request.getThumbnail(), newThumbnailId);
                if (post.getThumbnailId() != null) {
                    fileUtils.destroy(post.getThumbnailId(), FileType.image);
                }
                post.setThumbnailId(newThumbnailId);
                post.setThumbnailUrl(newThumbnailUrl);
            }
            postRepository.save(post);
            return Response.<PostBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .build();
        } catch (IOException e) {
            return Response.<PostBody>builder()
                    .status(400)
                    .msg(e.getMessage())
                    .build();
        }


    }

    public Response<PostBody> likeOrDislikePost(String postId, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        boolean isPostExisted = postRepository.findById(postId).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        Post post = postRepository.findById(postId).get();
        List<String> listLikedBy = post.getLikedBy();
        if (listLikedBy.contains(username)) {
            listLikedBy.remove(username);
        } else {
            listLikedBy.add(username);
        }
        post.setLikedBy(listLikedBy);

        postRepository.save(post);

        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    public Response<PostBody> createComment(String postId,
                                            CreateCommentRequest request,
                                            String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        boolean isPostExisted = postRepository.findById(postId).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        User user = userRepository.findByUsername(username).get();
        Post post = postRepository.findById(postId).get();
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
        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    public Response<PostBody> deleteComment(DeleteCommentRequest request, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        boolean isPostExisted = postRepository.findById(request.getPostId()).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        boolean isCommentExisted = commentRepository.findById(request.getCommentId()).isPresent();
        if (!isCommentExisted) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorCommentNotFound.getStatus())
                    .msg(StatusList.errorCommentNotFound.getMsg())
                    .build();
        }
        Post post = postRepository.findById(request.getPostId()).get();
        List<String> commentsId = post.getComments();
        if (!commentsId.contains(request.getCommentId())) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorCommentNotFoundOnProvidedPost.getStatus())
                    .msg(StatusList.errorCommentNotFoundOnProvidedPost.getMsg())
                    .build();
        }

        Comment comment = commentRepository.findById(request.getCommentId()).get();
        if (!comment.getOwnerUsername().equals(username)) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                    .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                    .build();
        }
        commentUtils.deleteComment(comment.getId());
        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    public Response<PostBody> deletePost(String postId, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        }
        boolean isPostExisted = postRepository.findById(postId).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg())
                    .build();
        }
        Post post = postRepository.findById(postId).get();
        if (!post.getOwnerUsername().equals(username)) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorUsernameDoesNotMatch.getStatus())
                    .msg(StatusList.errorUsernameDoesNotMatch.getMsg())
                    .build();
        }
        List<String> commentsId = post.getComments();
        commentsId.forEach(commentUtils::deleteComment);
        postRepository.deleteById(postId);
        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }


}

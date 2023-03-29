package com.example.musixBE.services.social;

import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.social.post.PostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.PostBody;
import com.example.musixBE.repositories.CommentRepository;
import com.example.musixBE.repositories.PostRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.FileType;
import com.example.musixBE.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final FileUtils fileUtils;
    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<PostBody> createPost(PostRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            boolean isUserExisted = userRepository.findByUsername(username).isPresent();
            if (!isUserExisted || username == null) {
                return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
            }
            var cloudId = username + "/social/post/" + UUID.randomUUID();
            User user = userRepository.findByUsername(username).get();
            var uploadUrl = fileUtils.upload(request.getFile(), cloudId);
            Post post = Post.builder()
                    .ownerId(user.getId())
                    .ownerUsername(user.getUsername())
                    .cloudId(cloudId)
                    .comments(new ArrayList<>())
                    .content(request.getContent())
                    .dateCreated(System.currentTimeMillis())
                    .lastModified(System.currentTimeMillis())
                    .likedBy(new ArrayList<>())
                    .fileType(request.getFileType())
                    .dataUrl(uploadUrl)
                    .build();
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
            if (!isUserExisted) {
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
            var newCloudId = username + "/social/post/" + UUID.randomUUID();
            var uploadUrl = fileUtils.upload(request.getFile(), newCloudId);
            fileUtils.destroy(post.getCloudId(), post.getFileType());
            post.setContent(request.getContent());
            post.setDataUrl(uploadUrl);
            post.setFileType(request.getFileType());
            post.setCloudId(newCloudId);
            post.setLastModified(System.currentTimeMillis());

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
}

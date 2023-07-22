package com.example.musixBE.services.social;

import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.social.post.PostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.ListPostBody;
import com.example.musixBE.payloads.responses.social.PostBody;
import com.example.musixBE.repositories.CommentRepository;
import com.example.musixBE.repositories.PostRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.EmailService;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.CommentUtils;
import com.example.musixBE.utils.FileType;
import com.example.musixBE.utils.FileUtils;
import com.example.musixBE.utils.PostStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final FileUtils fileUtils;
    private final CommentUtils commentUtils;
    private final EmailService emailService;
    MongoClient mongoClient = MongoClients.create("mongodb+srv://user:123456aA@clustermusic.wqqweqo.mongodb.net/musix");
    MongoDatabase database = mongoClient.getDatabase("musix");
    MongoCollection<Document> collection = database.getCollection("posts");
    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<PostBody> getPostById(String postId) {
        var post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return Response.<PostBody>builder().status(StatusList.errorPostNotFound.getStatus()).msg(StatusList.errorPostNotFound.getMsg()).build();
        }
        return Response.<PostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new PostBody(musixMapper.postToPostDTO(post.get()))).build();

    }

    public Response<ListPostBody> getPostsByUsername(String username, int page, int size) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Response.<ListPostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
        }
        var posts = postRepository.findByUsername(user.get().getUsername(), PageRequest.of(page, size, Sort.by("dateCreated").ascending()));
        return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(musixMapper.listPostToListPostDTO(posts))).build();
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
                    .fileName(request.getFileName())
                    .comments(new ArrayList<>())
                    .content(request.getContent())
                    .dateCreated(System.currentTimeMillis())
                    .lastModified(System.currentTimeMillis())
                    .likedBy(new ArrayList<>())
                    .fileUrl(dataUrl)
                    .postStatus(PostStatus.open)
                    .build();
            if (request.getThumbnail() != null) {
                var thumbnailId = username + "/social/post/thumbnail/" + cloudId;
                var thumbnailUrl = fileUtils.upload(request.getThumbnail(), thumbnailId);
                post.setThumbnailId(thumbnailId);
                post.setThumbnailUrl(thumbnailUrl);
            }
            postRepository.save(post);
            return Response.<PostBody>builder().status(StatusList.successService.getStatus())
                    .data(PostBody.builder().post(musixMapper.postToPostDTO(post)).build()).msg(StatusList.successService.getMsg()).build();
        } catch (IOException e) {
            return Response.<PostBody>builder().status(400).msg(e.getMessage()).build();
        }
    }

    public Response<PostBody> modifyPost(PostRequest request, String postId, String bearerToken) {

        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            boolean isUserExisted = userRepository.findByUsername(username).isPresent();
            if (!isUserExisted || username == null) {
                return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
            }

            boolean isPostExisted = postRepository.findById(postId).isPresent();
            if (!isPostExisted) {
                return Response.<PostBody>builder().status(StatusList.errorPostNotFound.getStatus()).msg(StatusList.errorPostNotFound.getMsg()).build();
            }

            Post post = postRepository.findById(postId).get();
            if (!username.equals(post.getOwnerUsername())) {
                return Response.<PostBody>builder().status(StatusList.errorUsernameDoesNotMatch.getStatus()).msg(StatusList.errorUsernameDoesNotMatch.getMsg()).build();
            }
            if (request.getContent() != null) {
                post.setContent(request.getContent());
            }
            if (request.getFileName() != null) {
                post.setFileName(request.getFileName());
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
            post.setLastModified(System.currentTimeMillis());
            postRepository.save(post);
            return Response.<PostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(PostBody.builder().post(musixMapper.postToPostDTO(post)).build()).build();
        } catch (IOException e) {
            return Response.<PostBody>builder().status(400).msg(e.getMessage()).build();
        }


    }

    public Response<PostBody> likeOrDislikePost(String postId, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
        }
        boolean isPostExisted = postRepository.findById(postId).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder().status(StatusList.errorPostNotFound.getStatus()).msg(StatusList.errorPostNotFound.getMsg()).build();
        }
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findByUsername(username).get();
        List<String> listLikedBy = post.getLikedBy();
        if (listLikedBy.contains(user.getId())) {
            listLikedBy.remove(user.getId());
        } else {
            listLikedBy.add(user.getId());
        }
        post.setLikedBy(listLikedBy);

        postRepository.save(post);

        return Response.<PostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).build();
    }


    public Response<PostBody> deletePost(String postId, String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted || username == null) {
            return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
        }
        boolean isPostExisted = postRepository.findById(postId).isPresent();
        if (!isPostExisted) {
            return Response.<PostBody>builder().status(StatusList.errorPostNotFound.getStatus()).msg(StatusList.errorPostNotFound.getMsg()).build();
        }
        Post post = postRepository.findById(postId).get();
        if (!post.getOwnerUsername().equals(username)) {
            return Response.<PostBody>builder().status(StatusList.errorUsernameDoesNotMatch.getStatus()).msg(StatusList.errorUsernameDoesNotMatch.getMsg()).build();
        }
        List<String> commentsId = post.getComments();
        commentsId.forEach(commentUtils::deleteComment);
        postRepository.deleteById(postId);
        return Response.<PostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).build();
    }


    public Response<ListPostBody> getAllPost() {
        var posts = postRepository.findAll();
        return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(musixMapper.listPostToListPostDTO(posts))).build();
    }

    public Response<ListPostBody> getPosts(int page, int size) {
        try {
            List<Post> posts = postRepository.findAll(PageRequest.of(page, size)).stream().toList();
            return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(musixMapper.listPostToListPostDTO(posts))).build();
        } catch (Exception e) {
            return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(musixMapper.listPostToListPostDTO(new ArrayList<>()))).build();
        }
    }

    public Response<ListPostBody> getPostByContent(String query, int page, int size) {
        try {

            List<Post> posts = postRepository.findByContent(query, PageRequest.of(page, size, Sort.by("dateCreated").descending()));
            return Response.<ListPostBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(new ListPostBody(musixMapper.listPostToListPostDTO(posts))).build();
        } catch (Exception e) {
            return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(new ArrayList<>())).build();
        }
    }

    public Response<ListPostBody> getPostsByFollowingUser(String bearerToken) {
        String username = jwtService.extractUsername(bearerToken.substring(7));
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted) {
            return Response.<ListPostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
        }
        List<User> miniVersionOfFollowings = userRepository.findByUsername(username).get().getFollowings();
        List<String> followingUsernames = new ArrayList<>();
        for (User following :
                miniVersionOfFollowings) {
            Optional<User> user = userRepository.findById(following.getId());
            if (user.isEmpty()) {
                continue;
            }
            followingUsernames.add(user.get().getUsername());
        }

        Bson filter = Filters.in("ownerUsername", followingUsernames);
        var results = collection.aggregate(List.of(
                Aggregates.match(filter)));
        List<Post> posts = new ArrayList<>();
        results.forEach(document -> posts.add(documentToPost(document)));
        Collections.shuffle(posts);
        return Response.<ListPostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(new ListPostBody(musixMapper.listPostToListPostDTO(posts)))
                .build();
    }

    public Response<ListPostBody> getTrendingPosts(int page, int size) {
        try {
            List<Post> posts = postRepository.findAll(PageRequest.of(page, size, Sort.by("likedBy").descending())).stream().toList();
            return Response.<ListPostBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(new ListPostBody(musixMapper.listPostToListPostDTO(posts))).build();
        } catch (Exception e) {
            return Response.<ListPostBody>builder().status(StatusList.successService.getStatus()).msg(StatusList.successService.getMsg()).data(new ListPostBody(new ArrayList<>())).build();

        }
    }

    public Response<PostBody> updatePostStatus(String postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return Response.<PostBody>builder()
                    .status(StatusList.errorPostNotFound.getStatus())
                    .msg(StatusList.errorPostNotFound.getMsg()).build();
        }
        Optional<User> user = userRepository.findById(post.get().getOwnerId());
        if (user.isEmpty()) {
            return Response.<PostBody>builder().status(StatusList.errorUsernameNotFound.getStatus()).msg(StatusList.errorUsernameNotFound.getMsg()).build();
        }
        PostStatus postStatus = post.get().getPostStatus();
        if (postStatus == PostStatus.open) {
            emailService.sendDeletedPostNotification(user.get(), post.get());
            post.get().setPostStatus(PostStatus.block);
        } else {
            emailService.sendActivatedPostNotification(user.get(), post.get());
            post.get().setPostStatus(PostStatus.open);
        }
        postRepository.save(post.get());
        return Response.<PostBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .data(new PostBody(musixMapper.postToPostDTO(post.get())))
                .build();
    }

    Post documentToPost(Document document) {
        return Post.builder()
                .id(document.get("_id").toString())
                .ownerId(document.get("ownerId").toString())
                .ownerUsername(document.get("ownerUsername").toString())
                .fileId(document.get("fileId").toString())
                .thumbnailId(document.get("thumbnailId").toString())
                .fileName(document.get("fileName").toString())
                .content(document.get("content").toString())
                .thumbnailUrl(document.get("thumbnailUrl").toString())
                .comments(document.getList("comments", String.class))
                .dateCreated(document.getLong("dateCreated"))
                .lastModified(document.getLong("lastModified"))
                .likedBy(document.getList("likedBy", String.class))
                .fileUrl(document.get("fileUrl").toString())
                .build();
    }


}

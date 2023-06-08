package com.example.musixBE.controllers.social;

import com.example.musixBE.payloads.requests.social.post.PostRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.ListPostBody;
import com.example.musixBE.payloads.responses.social.PostBody;
import com.example.musixBE.services.social.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/social/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<Response<ListPostBody>> getAllPost() {
        Response<ListPostBody> response = postService.getAllPost();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<Response<ListPostBody>> getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "0") int size) {
        Response<ListPostBody> response = postService.getPosts(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Response<PostBody>> getPostById(@PathVariable("postId") String postId) {
        Response<PostBody> response = postService.getPostById(postId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping()
    public ResponseEntity<Response<ListPostBody>> getPostsByUsername(@RequestParam("username") String username,
                                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "size", defaultValue = "5") int size) {
        Response<ListPostBody> response = postService.getPostsByUsername(username, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping()
    public ResponseEntity<Response<PostBody>> createPost(@ModelAttribute PostRequest request,
                                                         @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.createPost(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Response<PostBody>> modifyPost(@ModelAttribute PostRequest request,
                                                         @PathVariable("postId") String postId,
                                                         @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.modifyPost(request, postId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<Response<PostBody>> likeOrDislikePost(@PathVariable("postId") String postId,
                                                                @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.likeOrDislikePost(postId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<PostBody>> deletePost(@PathVariable("postId") String postId,
                                                         @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.deletePost(postId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/by-content")
    public ResponseEntity<Response<ListPostBody>> getPostsByContent(@RequestParam("query") String query,
                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        Response<ListPostBody> response = postService.getPostByContent(query, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping("/following")
    public ResponseEntity<Response<ListPostBody>> getPostsByFollowingUsers(@RequestHeader("Authorization") String bearerToken) {
        Response<ListPostBody> response = postService.getPostsByFollowingUser(bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/trending")
    public ResponseEntity<Response<ListPostBody>> getTrendingPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "5") int size) {
        Response<ListPostBody> response = postService.getTrendingPosts(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

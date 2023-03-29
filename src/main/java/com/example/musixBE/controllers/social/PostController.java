package com.example.musixBE.controllers.social;

import com.example.musixBE.payloads.requests.social.comment.CreateCommentRequest;
import com.example.musixBE.payloads.requests.social.post.DeleteCommentRequest;
import com.example.musixBE.payloads.requests.social.post.PostRequest;
import com.example.musixBE.payloads.responses.Response;
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

    @PostMapping("/comment/{postId}")
    public ResponseEntity<Response<PostBody>> commentPost(@PathVariable("postId") String postId,
                                                          @RequestBody CreateCommentRequest request,
                                                          @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.createComment(postId, request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<Response<PostBody>> deleteComment(@RequestBody DeleteCommentRequest request,
                                                            @RequestHeader("Authorization") String bearerToken) {
        Response<PostBody> response = postService.deleteComment(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<PostBody>> deletePost(@PathVariable("postId") String postId,
                                                         @RequestHeader("Authorization") String bearerToken){
        Response<PostBody>response = postService.deletePost(postId,bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

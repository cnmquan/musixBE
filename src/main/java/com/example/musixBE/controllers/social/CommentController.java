package com.example.musixBE.controllers.social;

import com.example.musixBE.payloads.requests.social.CreateCommentRequest;
import com.example.musixBE.payloads.requests.social.ModifyCommentRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.social.CommentBody;
import com.example.musixBE.services.social.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/social/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<Response<CommentBody>> getComment(@PathVariable("commentId") String commentId) {
        Response<CommentBody> response = commentService.getComment(commentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping()
    public ResponseEntity<Response<CommentBody>> createComment(@RequestBody CreateCommentRequest request, @RequestHeader("Authorization") String bearerToken) {
        var response = commentService.createComment(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping()
    public ResponseEntity<Response<CommentBody>> modifiedComment(@RequestBody ModifyCommentRequest request, @RequestHeader("Authorization") String bearerToken) {
        Response<CommentBody> response = commentService.modifiedComment(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/like/{commentId}")
    public ResponseEntity<Response<CommentBody>> likeOrDislikeComment(@PathVariable("commentId") String commentId, @RequestHeader("Authorization") String bearerToken) {
        Response<CommentBody> response = commentService.likeOrDislikeComment(commentId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reply/{commentId}")
    public ResponseEntity<Response<CommentBody>> replyComment(@PathVariable("commentId") String commentId,
                                                              @RequestBody CreateCommentRequest request,
                                                              @RequestHeader("Authorization") String bearerToken) {
        Response<CommentBody> response = commentService.reply(commentId, request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response<CommentBody>> delete(@PathVariable("commentId") String commentId,
                                                        @RequestHeader("Authorization") String bearerToken) {
        Response<CommentBody> response = commentService.delete(commentId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

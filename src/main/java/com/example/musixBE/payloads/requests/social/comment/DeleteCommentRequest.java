package com.example.musixBE.payloads.requests.social.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCommentRequest {
    private String postId;
    private String commentId;
}

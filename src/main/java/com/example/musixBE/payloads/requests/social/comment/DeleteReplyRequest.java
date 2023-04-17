package com.example.musixBE.payloads.requests.social.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReplyRequest {
    String commentId;
    String replyId;
}

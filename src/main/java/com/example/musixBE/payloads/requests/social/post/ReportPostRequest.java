package com.example.musixBE.payloads.requests.social.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostRequest {
    private String postId;
    private String userId;
    private String reason;
}

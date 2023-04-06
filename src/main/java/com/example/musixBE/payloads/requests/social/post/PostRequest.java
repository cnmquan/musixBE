package com.example.musixBE.payloads.requests.social.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String content;
    private String fileName;
    private MultipartFile file;
    private MultipartFile thumbnail;
}

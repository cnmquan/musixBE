package com.example.musixBE.payloads.requests.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioUploadRequest {
    private MultipartFile thumbnail;
    private MultipartFile audio;
    private String name;
    private String id;
}

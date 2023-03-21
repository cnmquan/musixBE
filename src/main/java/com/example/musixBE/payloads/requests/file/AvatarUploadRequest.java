package com.example.musixBE.payloads.requests.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarUploadRequest {
    private String id;
    private MultipartFile avatar;
}

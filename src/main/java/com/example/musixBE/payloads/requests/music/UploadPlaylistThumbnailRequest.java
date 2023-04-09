package com.example.musixBE.payloads.requests.music;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UploadPlaylistThumbnailRequest {
    private MultipartFile thumbnail;
}

package com.example.musixBE.controllers.file;

import com.example.musixBE.payloads.requests.file.AvatarUploadRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService service;
    @PostMapping("/upload/profile")
    public ResponseEntity<Response<ProfileBody>> uploadProfile(@ModelAttribute AvatarUploadRequest request) {
        var response = service.uploadAvatar(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

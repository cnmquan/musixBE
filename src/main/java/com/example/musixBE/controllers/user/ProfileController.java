package com.example.musixBE.controllers.user;

import com.example.musixBE.payloads.requests.user.GetProfileRequest;
import com.example.musixBE.payloads.requests.user.SearchProfileRequest;
import com.example.musixBE.payloads.requests.user.UploadAvatarRequest;
import com.example.musixBE.payloads.requests.user.UploadProfileRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ListProfileBody;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.services.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @GetMapping("/info")
    public ResponseEntity<Response<ProfileBody>> getInfo(@RequestBody GetProfileRequest request) {
        var response = service.getProfile(request.getId());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response<ListProfileBody>> getInfo(@RequestBody SearchProfileRequest request) {
        var response = service.searchProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/info")
    public ResponseEntity<Response<ProfileBody>> uploadInfo(
            @RequestBody UploadProfileRequest request
    ) {
        var response = service.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/avatar")
    public ResponseEntity<Response<ProfileBody>> uploadProfile(@ModelAttribute UploadAvatarRequest request) {
        var response = service.uploadAvatar(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

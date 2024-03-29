package com.example.musixBE.controllers.user;

import com.example.musixBE.payloads.requests.authentication.ChangePasswordRequest;
import com.example.musixBE.payloads.requests.authentication.SetAdminRequest;
import com.example.musixBE.payloads.requests.user.*;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.authentication.AuthenticationBody;
import com.example.musixBE.payloads.responses.user.ListProfileBody;
import com.example.musixBE.payloads.responses.user.ListUserDataBody;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.payloads.responses.user.UserDataBody;
import com.example.musixBE.services.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @GetMapping("/{userId}")
    public ResponseEntity<Response<ProfileBody>> getUserInfo(@PathVariable("userId") String userId) {
        var response = service.getUserProfile(userId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Response<ProfileBody>> getInfo(@RequestHeader("Authorization") String bearerToken) {
        var response = service.getProfile(bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response<ListProfileBody>> getInfo(@RequestBody SearchProfileRequest request) {
        var response = service.searchProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/info")
    public ResponseEntity<Response<ProfileBody>> uploadInfo(
            @RequestBody UploadProfileRequest request,
            @RequestHeader("Authorization") String bearerToken
    ) {
        var response = service.updateProfile(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PutMapping("/avatar")
    public ResponseEntity<Response<ProfileBody>> uploadProfile(@ModelAttribute UploadAvatarRequest request, @RequestHeader("Authorization") String bearerToken) {
        var response = service.uploadAvatar(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/follow/{followId}")
    public ResponseEntity<Response<ProfileBody>> followUser(@PathVariable("followId") String followId, @RequestHeader("Authorization") String bearerToken) {
        var response = service.followUser(followId, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String bearerToken) {
        var response = service.changePassword(request, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<ListUserDataBody>> getUsers() {
        var response = service.getUsers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/set-admin")
    public ResponseEntity<Response<AuthenticationBody>> setAdmin(
            @RequestBody SetAdminRequest request
    ) {
        var response = service.setAdmin(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/disable")
    public ResponseEntity<Response<UserDataBody>> disableUser(
            @RequestBody GetUserProfileRequest request
    ) {
        var response = service.disableUser(request.getId());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

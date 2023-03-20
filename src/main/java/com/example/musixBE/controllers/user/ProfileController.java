package com.example.musixBE.controllers.user;

import com.example.musixBE.payloads.requests.user.ProfileRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.services.user.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @PostMapping("/info")
    public ResponseEntity<Response<ProfileBody>> register(
            @RequestBody ProfileRequest request
    ) {
        var response = service.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);

    }
}

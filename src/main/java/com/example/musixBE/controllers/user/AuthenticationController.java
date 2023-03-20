package com.example.musixBE.controllers.user;

import com.example.musixBE.payloads.requests.authentication.AuthenticationRequest;
import com.example.musixBE.payloads.requests.authentication.LoginRequest;
import com.example.musixBE.payloads.requests.authentication.RegisterRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.authentication.AuthenticationBody;
import com.example.musixBE.services.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthenticationBody>> register(
            @RequestBody RegisterRequest request
    ) {
        var response = service.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<Response<AuthenticationBody>> login(
            @RequestBody LoginRequest request
    ) {
        var response = service.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response<AuthenticationBody>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        var response = service.authentication(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}

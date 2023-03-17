package com.example.musixBE.controllers;

import com.example.musixBE.payloads.requests.AuthenticationRequest;
import com.example.musixBE.payloads.requests.RegisterRequest;
import com.example.musixBE.payloads.responses.AuthenticationFailedResponse;
import com.example.musixBE.payloads.responses.AuthenticationResponse;
import com.example.musixBE.payloads.responses.AuthenticationSuccessResponse;
import com.example.musixBE.services.AuthenticationService;
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
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = service.register(request);
        if(response.getClass() == AuthenticationSuccessResponse.class){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(((AuthenticationFailedResponse)response).getStatus()).body(response);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authentication(request);
        if(response.getClass() == AuthenticationSuccessResponse.class){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(((AuthenticationFailedResponse)response).getStatus()).body(response);
        }
    }


}

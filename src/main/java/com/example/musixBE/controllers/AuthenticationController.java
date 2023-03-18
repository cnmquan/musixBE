package com.example.musixBE.controllers;

import com.example.musixBE.payloads.requests.AuthenticationRequest;
import com.example.musixBE.payloads.requests.RegisterRequest;
import com.example.musixBE.payloads.responses.FailedResponse;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.AuthenticationResponse;
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
    public ResponseEntity<Response> register(
            @RequestBody RegisterRequest request
    ) {
        Response response = service.register(request);
        if(response.getClass() == AuthenticationResponse.class){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(((FailedResponse)response).getStatus()).body(response);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        Response response = service.authentication(request);
        if(response.getClass() == AuthenticationResponse.class){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(((FailedResponse)response).getStatus()).body(response);
        }
    }


}

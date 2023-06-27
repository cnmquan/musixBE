package com.example.musixBE.controllers.user;

import com.example.musixBE.payloads.requests.authentication.AuthenticationRequest;
import com.example.musixBE.payloads.requests.authentication.LoginRequest;
import com.example.musixBE.payloads.requests.authentication.RegisterRequest;
import com.example.musixBE.payloads.requests.authentication.ResetPasswordRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.authentication.AuthenticationBody;
import com.example.musixBE.payloads.responses.authentication.ConfirmationBody;
import com.example.musixBE.payloads.responses.authentication.ResetPasswordBody;
import com.example.musixBE.services.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //This should return HTML page, so no it will return HTML code instead of ResponseEntity
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return service.confirm(token);
    }

    @PostMapping("/resend/{username}")
    public ResponseEntity<Response<ConfirmationBody>> resend(@PathVariable("username") String username) {
        var response = service.sendVerificationEmail(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reset/{email}")
    public ResponseEntity<Response<ResetPasswordBody>> requestResetPassword(@PathVariable("email") String email) {
        var response = service.requestResetPassword(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/reset")
    public ResponseEntity<Response<ResetPasswordBody>> resetPassword(@RequestBody ResetPasswordRequest request) {
        var response = service.resetPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

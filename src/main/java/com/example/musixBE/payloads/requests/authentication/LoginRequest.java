package com.example.musixBE.payloads.requests.authentication;

import com.example.musixBE.payloads.requests.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends Request {
    private String username;
    private String password;
}

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
public class AuthenticationRequest extends Request {
    private String token;
}

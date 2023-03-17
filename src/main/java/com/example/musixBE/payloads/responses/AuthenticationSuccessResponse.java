package com.example.musixBE.payloads.responses;

import com.example.musixBE.models.Token;
import com.example.musixBE.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationSuccessResponse extends AuthenticationResponse {
    private User user;
    private Token token;
}

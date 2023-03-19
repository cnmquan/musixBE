package com.example.musixBE.payloads.responses;

import com.example.musixBE.models.token.TokenDTO;
import com.example.musixBE.models.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends Response {
    private UserDTO user;
    private TokenDTO token;
}

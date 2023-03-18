package com.example.musixBE.payloads.responses;

import com.example.musixBE.models.Token;
import com.example.musixBE.models.TokenDTO;
import com.example.musixBE.models.UserDTO;
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

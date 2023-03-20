package com.example.musixBE.payloads.responses.authentication;

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
public class AuthenticationBody {
    private UserDTO user;
    private TokenDTO token;
}

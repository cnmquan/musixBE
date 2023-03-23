package com.example.musixBE.models.token;

import com.example.musixBE.models.user.UserDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDTO {
    private String id;
    private String token;
    private TokenType tokenType;
    private boolean isExpired;
    private long dateExpired;
    private long dateCreated;
    private long confirmedAt;
    private UserDTO user;
}

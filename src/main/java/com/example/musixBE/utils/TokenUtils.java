package com.example.musixBE.utils;

import com.example.musixBE.models.token.Token;
import com.example.musixBE.models.token.TokenType;
import com.example.musixBE.models.user.User;
import com.example.musixBE.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@AllArgsConstructor
public class TokenUtils {
    private final TokenRepository tokenRepository;

    public Token saveConfirmationToken(User user, String confirmationToken) {
        var token = Token.builder()
                .user(User.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .token(confirmationToken)
                .tokenType(TokenType.CONFIRMATION)
                .isExpired(false)
                .revoked(false)
                .dateCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .dateExpired(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
        return tokenRepository.save(token);
    }

    public Token saveResetPasswordToken(User user, String resetPasswordToken) {
        var token = Token.builder()
                .user(User.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .token(resetPasswordToken)
                .tokenType(TokenType.RESET_PASSWORD)
                .isExpired(false)
                .revoked(false)
                .dateCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .dateExpired(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
        return tokenRepository.save(token);
    }

    public Token saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(User.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .revoked(false)
                .dateExpired(1000L * 30 * 24 * 60 * 60 + System.currentTimeMillis())
                .dateCreated(System.currentTimeMillis())
                .build();
        return tokenRepository.save(token);
    }


}

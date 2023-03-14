package com.example.musixBE.services;

import com.example.musixBE.models.Role;
import com.example.musixBE.models.Token;
import com.example.musixBE.models.TokenType;
import com.example.musixBE.models.User;
import com.example.musixBE.payloads.requests.AuthenticationRequest;
import com.example.musixBE.payloads.requests.RegisterRequest;
import com.example.musixBE.payloads.responses.AuthenticationResponse;
import com.example.musixBE.repositories.TokenRepository;
import com.example.musixBE.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .phoneNumber(request.getPhoneNumber())
                .tokens(new ArrayList<>())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var userSaved = userRepository.save(user);
        var jwtToken = jwtService.generatedToken(user);
        var token = saveUserToken(userSaved, jwtToken);
        var userSaveUpdated = updateUserToken(userSaved, token);
        return  AuthenticationResponse.builder()
                .user(userSaveUpdated)
                .token(jwtToken)
                .build();
    }

    private Token saveUserToken(User user, String jwtToken) {
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
                .dateExpired(1000L * 30 * 24 * 60 * 60)
                .build();
        return tokenRepository.save(token);
    }

    private User updateUserToken(User user, Token token){
        var userSaved = userRepository.findByUsername(user.getUsername());
        if (userSaved.isPresent()) {
            List<Token> tokens = userSaved.get().getTokens();
            tokens.add(Token.builder()
                    .id(token.getId())
                    .token(token.getToken())
                    .isExpired(token.isExpired())
                    .dateExpired(token.getDateExpired())
                    .tokenType(token.getTokenType())
                    .build());
            userSaved.get().setTokens(tokens);
            return userRepository.save(userSaved.get());
        }
        return null;
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        System.out.println(user.getId());
        var token = getValidToken(user);
        if(token.isPresent()){
            return  AuthenticationResponse.builder()
                    .token(token.get().getToken())
                    .user(user)
                    .build();
        } else {
            var jwtToken = jwtService.generatedToken(user);
            var tokenUpdate = saveUserToken(user, jwtToken);
            var userUpdated = updateUserToken(user, tokenUpdate);
            return  AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(userUpdated)
                    .build();
        }
    }

    private Optional<Token> getValidToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
        if(validUserTokens == null) return Optional.empty();
        if(validUserTokens.isEmpty()) return Optional.empty();
        validUserTokens.sort((a, b) -> Math.toIntExact(b.getDateExpired() - a.getDateExpired()));
        for (Token validUserToken : validUserTokens) {
            if(!validUserToken.isExpired() && !validUserToken.isRevoked()){
                return Optional.of(validUserToken);
            }
        }
        return  Optional.empty();
    }
}

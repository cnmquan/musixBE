package com.example.musixBE.services;

import com.example.musixBE.models.Role;
import com.example.musixBE.models.Token;
import com.example.musixBE.models.TokenType;
import com.example.musixBE.models.User;
import com.example.musixBE.payloads.requests.AuthenticationRequest;
import com.example.musixBE.payloads.requests.RegisterRequest;
import com.example.musixBE.payloads.responses.AuthenticationFailedResponse;
import com.example.musixBE.payloads.responses.AuthenticationResponse;
import com.example.musixBE.payloads.responses.AuthenticationSuccessResponse;
import com.example.musixBE.repositories.TokenRepository;
import com.example.musixBE.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
        // Check username is existed in database
        boolean isExistedUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isEmpty();
        if (!isExistedUser) {
            // Catch username is existed in database
            return AuthenticationFailedResponse.builder()
                    .status(452)
                    .msg("Username is existed")
                    .build();
        }

        try {
            var user = User.builder()
                    .username(request.getUsername())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .birthday(request.getBirthday())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .followings(new ArrayList<>())
                    .followers(new ArrayList<>())
                    .role(Role.USER)
                    .build();

            // Save user info to db
            var userSaved = userRepository.save(user);

            // Create jwt token and save token to db
            var jwtToken = jwtService.generatedToken(user);
            var token = saveUserToken(userSaved, jwtToken);

            // Response
            return AuthenticationSuccessResponse.builder()
                    .user(userSaved)
                    .token(token)
                    .build();
        } catch (Exception e) {
            // Other Exception
            return AuthenticationFailedResponse.builder()
                    .status(499)
                    .msg("Error while save data")
                    .build();
        }
    }

    private Token saveUserToken(User user, String jwtToken) {
        Date date = new Date();
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
                .dateExpired(1000L * 30 * 24 * 60 * 60 + date.getTime())
                .dateCreated(date.getTime())
                .build();
        return tokenRepository.save(token);
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        try {
            // Get User from Username
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

            try {
                // Check Username and Password correct
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );
            } catch (AuthenticationException exception) {
                return AuthenticationFailedResponse.builder()
                        .status(403).msg("Password Not Correct")
                        .build();
            }

            // Get Token from Database
            var token = getValidToken(user);
            if (token != null) {
                return AuthenticationSuccessResponse.builder()
                        .token(token)
                        .user(user)
                        .build();
            } else {
                var jwtToken = jwtService.generatedToken(user);
                var tokenSaved = saveUserToken(user, jwtToken);
                return AuthenticationSuccessResponse.builder()
                        .token(tokenSaved)
                        .user(user)
                        .build();
            }
        } catch (UsernameNotFoundException exception) {
            return AuthenticationFailedResponse.builder()
                    .status(453)
                    .msg("Username Not Found")
                    .build();
        } catch (Exception e) {
            // Other Exception
            return AuthenticationFailedResponse.builder()
                    .status(499)
                    .msg("Error while save data")
                    .build();
        }

    }

    private Token getValidToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
        if (validUserTokens == null) return null;
        if (validUserTokens.isEmpty()) return null;
        System.out.println(validUserTokens.size());
        validUserTokens.sort((a, b) -> Math.toIntExact(b.getDateExpired() - a.getDateExpired()));
        Date date = new Date();
        for (Token validUserToken : validUserTokens) {
            if(date.getTime() >= validUserToken.getDateExpired()){
                validUserToken.setExpired(true);
                tokenRepository.save(validUserToken);
                continue;
            }
            if (!validUserToken.isExpired() && !validUserToken.isRevoked()) {
                return validUserToken;
            }
        }
        return null;
    }
}

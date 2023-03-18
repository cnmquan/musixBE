package com.example.musixBE.services;

import com.example.musixBE.models.*;
import com.example.musixBE.payloads.requests.AuthenticationRequest;
import com.example.musixBE.payloads.requests.RegisterRequest;
import com.example.musixBE.payloads.responses.FailedResponse;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.AuthenticationResponse;
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

    private final MusixMapper musixMapper = MusixMapper.INSTANCE;


    public Response register(RegisterRequest request) {
        // Check username is existed in database
        boolean isExistedUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isEmpty();
        if (!isExistedUser) {
            // Catch username is existed in database
            return FailedResponse.builder()
                    .status(StatusList.errorUsernameExisted.getStatus())
                    .msg(StatusList.errorUsernameExisted.getMsg())
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
            return AuthenticationResponse.builder()
                    .user(musixMapper.userToUserDTO(user))
                    .token(musixMapper.tokenToTokenDTO(token))
                    .build();
        } catch (Exception e) {
            // Other Exception
            return FailedResponse.builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(StatusList.errorService.getMsg())
                    .build();
        }
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
                .dateExpired(1000L * 30 * 24 * 60 * 60 + System.currentTimeMillis())
                .dateCreated(System.currentTimeMillis())
                .build();
        return tokenRepository.save(token);
    }

    public Response authentication(AuthenticationRequest request) {
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
                return FailedResponse.builder()
                        .status(StatusList.errorPasswordNotCorrect.getStatus())
                        .msg(StatusList.errorPasswordNotCorrect.getMsg())
                        .build();
            }
            // Get Token from Database
            UserDTO userDTO = musixMapper.userToUserDTO(user);
            var token = getValidToken(user);
            if (token != null) {
                TokenDTO tokenDTO = musixMapper.tokenToTokenDTO(token);
                return AuthenticationResponse.builder()
                        .token(tokenDTO)
                        .user(userDTO)
                        .build();
            } else {
                var jwtToken = jwtService.generatedToken(user);
                var tokenSaved = saveUserToken(user, jwtToken);
                TokenDTO tokenDTO = musixMapper.tokenToTokenDTO(tokenSaved);
                return AuthenticationResponse.builder()
                        .token(tokenDTO)
                        .user(userDTO)
                        .build();
            }
        } catch (UsernameNotFoundException exception) {
            return FailedResponse.builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        } catch (Exception e) {
            // Other Exception
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return FailedResponse.builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(StatusList.errorService.getMsg())
                    .build();
        }

    }

    private Token getValidToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
        if (validUserTokens == null) return null;
        if (validUserTokens.isEmpty()) return null;
        validUserTokens.sort((a, b) -> Math.toIntExact(b.getDateExpired() - a.getDateExpired()));
        for (Token validUserToken : validUserTokens) {
            if(!jwtService.isTokenValid(validUserToken.getToken(), user)){
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

package com.example.musixBE.services.user;

import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.token.Token;
import com.example.musixBE.models.token.TokenType;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.models.user.Role;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.authentication.AuthenticationRequest;
import com.example.musixBE.payloads.requests.authentication.LoginRequest;
import com.example.musixBE.payloads.requests.authentication.RegisterRequest;
import com.example.musixBE.payloads.requests.authentication.ResetPasswordRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.authentication.AuthenticationBody;
import com.example.musixBE.payloads.responses.authentication.ConfirmationBody;
import com.example.musixBE.payloads.responses.authentication.ResetPasswordBody;
import com.example.musixBE.repositories.TokenRepository;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.EmailService;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.EmailTemplateProvider;
import com.example.musixBE.utils.RandomString;
import com.example.musixBE.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenUtils tokenUtils;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<ResetPasswordBody> requestResetPassword(String email) {
        // Create confirmation token and save token to db

        String randomToken;
        boolean isUserExisted = userRepository.findByEmail(email).isPresent();
        if (!isUserExisted) {
            return Response.<ResetPasswordBody>builder()
                    .status(StatusList.errorEmailNotFound.getStatus())
                    .msg(StatusList.errorEmailNotFound.getMsg())
                    .build();
        }
        var user = userRepository.findByEmail(email).get();
        Token token = getValidTokenByUserAndType(user, TokenType.RESET_PASSWORD);
        if (token == null) {
            randomToken = RandomString.getAlphaNumericString(6);
            tokenUtils.saveResetPasswordToken(user, randomToken);
        } else {
            randomToken = token.getToken();
        }
        emailService.sendResetPasswordEmail(user, randomToken);
        return Response.<ResetPasswordBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }

    // This should return an HTML String
    public String confirm(String confirmationToken) {
        boolean isConfirmationTokenExisted = tokenRepository.findByToken(confirmationToken).isPresent();
        if (!isConfirmationTokenExisted) {
            return EmailTemplateProvider.buildErrorPage();
        }
        var token = tokenRepository.findByToken(confirmationToken).get();
        if (token.getDateExpired() < LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
            return EmailTemplateProvider.buildErrorPage();
        }
        var user = userRepository.findByUsername(token.getUser().getUsername()).get();
        user.setEnabled(true);
        tokenRepository.save(token);
        userRepository.save(user);
        return EmailTemplateProvider.buildSuccessPage();
    }

    public Response<AuthenticationBody> register(RegisterRequest request) {
        // Check username is existed in database
        boolean isExistedUser = userRepository.findByUsername(request.getUsername()).isEmpty();
        if (!isExistedUser) {
            // Catch username is existed in database
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorUsernameExisted.getStatus())
                    .msg(StatusList.errorUsernameExisted.getMsg())
                    .build();
        }

        try {
            var profile = Profile.builder()
                    .fullName(request.getFullName())
                    .birthday(request.getBirthday())
                    .phoneNumber(request.getPhoneNumber())
                    .build();
            var user = User.builder()
                    .username(request.getUsername())
                    .profile(profile)
                    .email(request.getEmail())
                    .enabled(false)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .followings(new ArrayList<>())
                    .followers(new ArrayList<>())
                    .role(Role.USER)
                    .build();

            // Save user info to db
            var userSaved = userRepository.save(user);

            // Create jwt token and save token to db
            var jwtToken = jwtService.generatedToken(user);
            var token = tokenUtils.saveUserToken(userSaved, jwtToken);
            sendVerificationEmail(userSaved.getUsername());

            // Response
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(AuthenticationBody.builder()
                            .user(musixMapper.userToUserDTO(user))
                            .token(musixMapper.tokenToTokenDTO(token))
                            .build())
                    .build();
        } catch (Exception e) {
            // Other Exception
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(StatusList.errorService.getMsg())
                    .build();
        }
    }

    public Response<ConfirmationBody> sendVerificationEmail(String username) {
        // Create confirmation token and save token to db
        boolean isUserExisted = userRepository.findByUsername(username).isPresent();
        if (!isUserExisted) {
            return Response.<ConfirmationBody>builder()
                    .status(StatusList.errorUserIdNotFound.getStatus())
                    .msg(StatusList.errorUserIdNotFound.getMsg())
                    .build();
        }
        var user = userRepository.findByUsername(username).get();
        String randomToken;
        Token token = getValidTokenByUserAndType(user, TokenType.CONFIRMATION);
        if (token == null) {
            randomToken = UUID.randomUUID().toString();
            tokenUtils.saveConfirmationToken(user, randomToken);
        } else {
            randomToken = token.getToken();
        }
        String verificationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + randomToken;
        //Send verification email
        emailService.sendVerificationLink(user, verificationLink);
        return Response.<ConfirmationBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }


    public Response<AuthenticationBody> authentication(AuthenticationRequest request) {
        try {
            var token = tokenRepository.findByToken(request.getToken())
                    .orElseThrow(() -> new Exception(StatusList.errorTokenNotFound.getMsg()));

            if (token.isRevoked() || token.isExpired()) {
                throw new Exception(StatusList.errorTokenNotValid.getMsg());
            }

            var user = userRepository.findByUsername(token.getUser().getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(StatusList.errorUsernameNotFound.getMsg()));

            if (!jwtService.isTokenValid(token.getToken(), user)) {
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
                throw new Exception(StatusList.errorTokenNotValid.getMsg());
            }

            return Response.<AuthenticationBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(AuthenticationBody.builder()
                            .user(musixMapper.userToUserDTO(user))
                            .token(musixMapper.tokenToTokenDTO(token))
                            .build())
                    .build();
        } catch (UsernameNotFoundException exception) {
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorTokenNotFound.getMsg())) {
                return Response.<AuthenticationBody>builder()
                        .status(StatusList.errorTokenNotFound.getStatus())
                        .msg(StatusList.errorTokenNotFound.getMsg())
                        .build();
            } else if (e.getMessage().equals(StatusList.errorTokenNotValid.getMsg())) {
                return Response.<AuthenticationBody>builder()
                        .status(StatusList.errorTokenNotValid.getStatus())
                        .msg(StatusList.errorTokenNotValid.getMsg())
                        .build();
            } else {
                return Response.<AuthenticationBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }

    }

    public Response<AuthenticationBody> login(LoginRequest request) {
        try {
            // Get User from Username
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(StatusList.errorUsernameNotFound.getMsg()));
            // Check Username and Password correct
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get Token from Database
            var token = getValidTokenByUserAndType(user, TokenType.BEARER);
            if (token != null) {
                return Response.<AuthenticationBody>builder()
                        .status(StatusList.successService.getStatus())
                        .msg(StatusList.successService.getMsg())
                        .data(AuthenticationBody.builder()
                                .user(musixMapper.userToUserDTO(user))
                                .token(musixMapper.tokenToTokenDTO(token))
                                .build())
                        .build();
            } else {
                var jwtToken = jwtService.generatedToken(user);
                var tokenSaved = tokenUtils.saveUserToken(user, jwtToken);
                return Response.<AuthenticationBody>builder()
                        .status(StatusList.successService.getStatus())
                        .msg(StatusList.successService.getMsg())
                        .data(AuthenticationBody.builder()
                                .user(musixMapper.userToUserDTO(user))
                                .token(musixMapper.tokenToTokenDTO(tokenSaved))
                                .build())
                        .build();
            }
        } catch (UsernameNotFoundException exception) {
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorUsernameNotFound.getStatus())
                    .msg(StatusList.errorUsernameNotFound.getMsg())
                    .build();
        } catch (AuthenticationException exception) {
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorPasswordNotCorrect.getStatus())
                    .msg(exception.getMessage())
                    .build();
        } catch (Exception e) {
            System.out.println(e);
            return Response.<AuthenticationBody>builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(StatusList.errorService.getMsg())
                    .build();
        }

    }

    private Token getValidTokenByUserAndType(User user, TokenType tokenType) {
        long currentTimeInMillis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var validUserTokens = tokenRepository.findAllValidTokenByUserAndTokenType(user.getUsername(), tokenType.name());
        if (validUserTokens == null) return null;
        if (validUserTokens.isEmpty()) return null;
        validUserTokens.sort((a, b) -> Math.toIntExact(b.getDateCreated() - a.getDateCreated()));
        for (Token validUserToken : validUserTokens) {
            if (validUserToken.getDateExpired() < currentTimeInMillis) {
                validUserToken.setExpired(true);
                validUserToken.setRevoked(true);
                tokenRepository.save(validUserToken);
                continue;
            }
            if (!validUserToken.isExpired() && !validUserToken.isRevoked()) {
                return validUserToken;
            }

        }
        return null;
    }

    private Token getValidToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
        if (validUserTokens == null) return null;
        if (validUserTokens.isEmpty()) return null;
        validUserTokens.sort((a, b) -> Math.toIntExact(b.getDateExpired() - a.getDateExpired()));
        for (Token validUserToken : validUserTokens) {
            if (!jwtService.isTokenValid(validUserToken.getToken(), user)) {
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

    public Response<ResetPasswordBody> resetPassword(ResetPasswordRequest request) {
        //check valid token
        boolean isExistedToken = tokenRepository.findByToken(request.getToken()).isPresent();
        if (!isExistedToken) {
            return Response.<ResetPasswordBody>builder()
                    .status(StatusList.errorTokenNotFound.getStatus())
                    .msg(StatusList.errorTokenNotFound.getMsg())
                    .build();
        }
        Token token = tokenRepository.findByToken(request.getToken()).get();
        if (token.getDateExpired() < LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
            return Response.<ResetPasswordBody>builder()
                    .status(StatusList.errorTokenNotValid.getStatus())
                    .msg(StatusList.errorTokenNotValid.getMsg())
                    .build();
        }
        if (!request.getEmail().equals(token.getUser().getEmail())) {
            return Response.<ResetPasswordBody>builder()
                    .status(StatusList.errorTokenDoesNotMatch.getStatus())
                    .msg(StatusList.errorTokenDoesNotMatch.getMsg())
                    .build();
        }
        boolean isUserExisted = userRepository.findByEmail(request.getEmail()).isPresent();
        if (!isUserExisted) {
            return Response.<ResetPasswordBody>builder()
                    .status(StatusList.errorEmailNotFound.getStatus())
                    .msg(StatusList.errorEmailNotFound.getMsg())
                    .build();
        }
        var user = userRepository.findByEmail(request.getEmail()).get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return Response.<ResetPasswordBody>builder()
                .status(StatusList.successService.getStatus())
                .msg(StatusList.successService.getMsg())
                .build();
    }
}

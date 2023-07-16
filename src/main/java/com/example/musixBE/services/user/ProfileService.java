package com.example.musixBE.services.user;

import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.models.user.Role;
import com.example.musixBE.models.user.User;
import com.example.musixBE.models.user.UserDataDTO;
import com.example.musixBE.payloads.requests.authentication.ChangePasswordRequest;
import com.example.musixBE.payloads.requests.authentication.SetAdminRequest;
import com.example.musixBE.payloads.requests.user.SearchProfileRequest;
import com.example.musixBE.payloads.requests.user.UploadAvatarRequest;
import com.example.musixBE.payloads.requests.user.UploadProfileRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.authentication.AuthenticationBody;
import com.example.musixBE.payloads.responses.user.ListProfileBody;
import com.example.musixBE.payloads.responses.user.ListUserDataBody;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.payloads.responses.user.UserDataBody;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.JwtService;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.utils.FileUtils;
import com.example.musixBE.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Update Email (maybe)
 * Update Profile
 * Update Avatar
 * */
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    private final FileUtils fileUtils;

    private final TokenUtils tokenUtils;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public Response<ProfileBody> getUserProfile(String id) {
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
            return  Response.<ProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ProfileBody.builder()
                            .user(musixMapper.userToUserDTO(user))
                            .build())
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ProfileBody> getProfile(String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            return  Response.<ProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ProfileBody.builder()
                            .user(musixMapper.userToUserDTO(user))
                            .build())
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ProfileBody> updateProfile(UploadProfileRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var profile = Profile.builder()
                    .fullName(request.getFullName() != null ? request.getFullName() : user.getProfile().getFullName())
                    .phoneNumber(
                            request.getPhoneNumber() != null ? request.getPhoneNumber() : user.getProfile().getPhoneNumber()
                    )
                    .birthday(
                            request.getBirthday() != null ? request.getBirthday() : user.getProfile().getBirthday()
                    )
                    .build();
            user.setProfile(profile);
            var userSaved = userRepository.save(user);
            return  Response.<ProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ProfileBody.builder()
                            .user(musixMapper.userToUserDTO(userSaved))
                            .build())
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ProfileBody> uploadAvatar(UploadAvatarRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var avatarId = user.getProfile().getAvatarId();
            if(avatarId == null){
                avatarId = user.getId()+"/profile/avatar";
                user.getProfile().setAvatarId(avatarId);
            }
            var uploadUrl = fileUtils.upload(request.getAvatar(), avatarId);
            user.getProfile().setAvatarUri(uploadUrl);
            var userSaved = userRepository.save(user);
            return  Response.<ProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ProfileBody.builder()
                            .user(musixMapper.userToUserDTO(userSaved))
                            .build())
                    .build();
        }
        catch (IOException e){
            return Response.<ProfileBody>builder()
                    .status(400)
                    .msg(e.getMessage())
                    .build();
        }
        catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListProfileBody> searchProfile(SearchProfileRequest request) {
        try {
            var users = userRepository.findByFullName(request.getFullName());
            return  Response.<ListProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListProfileBody.builder()
                            .users(users.stream().map(user -> musixMapper.userToUserDTO(user.get())).toList())
                            .build())
                    .build();
        }
        catch (Exception e) {
            return Response.<ListProfileBody>builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(e.getMessage())
                    .build();
        }
    }

    public Response<ProfileBody> followUser(String followId, String bearerToken) {
        try {
            // Get Info User and User that user is followed
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            var userFollowing = userRepository.findById(followId)
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));

            // Get List Following of User and Follower of Following User
            var userFollowingList = user.getFollowings();
            var userFollowerList = userFollowing.getFollowers();

            // Check the Following User is in List Following of User
            boolean isExisted = false;
            for (var userItem : userFollowingList) {
                // If Existed then unfollow
                if(Objects.equals(userItem.getId(), userFollowing.getId())){
                    userFollowingList.remove(userItem);
                    isExisted = true;
                    break;
                }
            }

           // If don't Exist then follow
            if(!isExisted){
                userFollowingList.add(User.builder()
                                .id(userFollowing.getId())
                                .profile(userFollowing.getProfile())
                        .build());


                userFollowerList.add(User.builder()
                        .id(user.getId())
                        .profile(user.getProfile())
                        .build());

            } else {
                // Remove user in follower list of following user
                for (var userItem : userFollowerList) {
                    if(Objects.equals(userItem.getId(), user.getId())){
                        userFollowerList.remove(userItem);
                        break;
                    }
                }
            }

            // Update user and following user
            user.setFollowings(userFollowingList);
            userRepository.save(user);
            userFollowing.setFollowers(userFollowerList);
            userRepository.save(userFollowing);

            // Return
            return  Response.<ProfileBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ProfileBody.builder()
                            .user(musixMapper.userToUserDTO(user))
                            .build())
                    .build();
        } catch (Exception e) {
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            }
            else if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response changePassword(ChangePasswordRequest request, String bearerToken) {
        try {
            final String username = jwtService.extractUsername(bearerToken.substring(7));
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception(StatusList.errorUsernameNotFound.getMsg()));
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                final var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new Exception(StatusList.errorPasswordNotCorrect.getMsg());
            }
            userRepository.save(user);
            return Response.builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .build();
        } catch(Exception e){
            if(e.getMessage().equals(StatusList.errorUsernameNotFound.getMsg())){
                return Response.builder()
                        .status(StatusList.errorUsernameNotFound.getStatus())
                        .msg(StatusList.errorUsernameNotFound.getMsg())
                        .build();
            } else if (e.getMessage().equals(StatusList.errorPasswordNotCorrect.getMsg())){
                return Response.builder()
                        .status(StatusList.errorPasswordNotCorrect.getStatus())
                        .msg(StatusList.errorPasswordNotCorrect.getMsg())
                        .build();
            }
            else {
                return Response.builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(e.getMessage())
                        .build();
            }
        }
    }

    public Response<ListUserDataBody> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDataDTO> usersData = users.stream().map((user) -> {
                return UserDataDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .fullName(user.getProfile().getFullName())
                        .role(user.getRole())
                        .enable(user.isEnabled())
                        .build();
            }).toList();
            return  Response.<ListUserDataBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(ListUserDataBody.builder()
                            .users(usersData)
                            .build())
                    .build();
        } catch (Exception e) {
            return Response.<ListUserDataBody>builder()
                    .status(StatusList.errorService.getStatus())
                    .msg(e.getMessage())
                    .build();
        }
    }

    public Response<AuthenticationBody> setAdmin(SetAdminRequest request) {
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
                    .fullName(request.getName())
                    .build();
            var user = User.builder()
                    .username(request.getUsername())
                    .profile(profile)
                    .enabled(true)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .followings(new ArrayList<>())
                    .followers(new ArrayList<>())
                    .role(Role.ADMIN)
                    .build();

            // Save user info to db
            var userSaved = userRepository.save(user);

            // Create jwt token and save token to db
            var jwtToken = jwtService.generatedToken(user);
            var token = tokenUtils.saveUserToken(userSaved, jwtToken);

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

    public Response<UserDataBody> disableUser(String id) {
        // Check username is existed in database

        try {
            var user = userRepository.findById(id).orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
            user.setEnabled(!user.isEnabled());
            var userSaved = userRepository.save(user);
            var userDataDTO = UserDataDTO.builder()
                    .id(userSaved.getId())
                    .email(userSaved.getEmail())
                    .username(userSaved.getUsername())
                    .fullName(userSaved.getProfile().getFullName())
                    .role(userSaved.getRole())
                    .enable(userSaved.isEnabled())
                    .build();

            return Response.<UserDataBody>builder()
                    .status(StatusList.successService.getStatus())
                    .msg(StatusList.successService.getMsg())
                    .data(UserDataBody.builder()
                            .user(userDataDTO)
                            .build())
                    .build();
        }
        catch (Exception e) {
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())) {
                return Response.<UserDataBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<UserDataBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }
}

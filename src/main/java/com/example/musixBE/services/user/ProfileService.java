package com.example.musixBE.services.user;

import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.models.user.User;
import com.example.musixBE.payloads.requests.authentication.ChangePasswordRequest;
import com.example.musixBE.payloads.requests.user.FollowUserRequest;
import com.example.musixBE.payloads.requests.user.SearchProfileRequest;
import com.example.musixBE.payloads.requests.user.UploadAvatarRequest;
import com.example.musixBE.payloads.requests.user.UploadProfileRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ListProfileBody;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.MusixMapper;
import com.example.musixBE.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    public Response<ProfileBody> getProfile(String id) {
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
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }

    public Response<ProfileBody> updateProfile(UploadProfileRequest request) {
        try {
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
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
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }

    public Response<ProfileBody> uploadAvatar(UploadAvatarRequest request) {
        try {
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
            var avatarId = user.getProfile().getAvatarId();
            if(avatarId == null){
                avatarId = user.getId()+"/profile/avatar";
                user.getProfile().setAvatarId(avatarId);
            }
            var uploadUrl = fileService.upload(request.getAvatar(), avatarId);
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
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
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
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ListProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ListProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }

    public Response<ProfileBody> followUser(FollowUserRequest request) {
        try {
            // Get Info User and User that user is followed
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
            var userFollowing = userRepository.findById(request.getFollowId())
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
            if (e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
                        .build();
            } else {
                return Response.<ProfileBody>builder()
                        .status(StatusList.errorService.getStatus())
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }

    public Response changePassword(ChangePasswordRequest request) {
        try {
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
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
            if(e.getMessage().equals(StatusList.errorUserIdNotFound.getMsg())){
                return Response.builder()
                        .status(StatusList.errorUserIdNotFound.getStatus())
                        .msg(StatusList.errorUserIdNotFound.getMsg())
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
                        .msg(StatusList.errorService.getMsg())
                        .build();
            }
        }
    }
}

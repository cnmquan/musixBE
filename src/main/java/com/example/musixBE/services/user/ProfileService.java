package com.example.musixBE.services.user;

import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.Profile;
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
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public Response<ProfileBody> getProfile(String id) {
        System.out.println(id);
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
                            .users(users.stream().map(user -> {
                               return musixMapper.userToUserDTO(user.get());
                            }).toList())
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
}

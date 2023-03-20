package com.example.musixBE.services.user;

import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.models.user.Profile;
import com.example.musixBE.payloads.requests.user.ProfileRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.MusixMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Response<ProfileBody> updateProfile(ProfileRequest request) {
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
}

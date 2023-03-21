package com.example.musixBE.services.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.musixBE.models.status.StatusList;
import com.example.musixBE.payloads.requests.file.AvatarUploadRequest;
import com.example.musixBE.payloads.responses.Response;
import com.example.musixBE.payloads.responses.user.ProfileBody;
import com.example.musixBE.repositories.UserRepository;
import com.example.musixBE.services.MusixMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final Cloudinary cloudinary;

    private final UserRepository userRepository;

    private final MusixMapper musixMapper = MusixMapper.INSTANCE;

    public Response<ProfileBody> uploadAvatar(AvatarUploadRequest request) {
        try {
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new Exception(StatusList.errorUserIdNotFound.getMsg()));
            var avatarId = user.getProfile().getAvatarId();
            if(avatarId == null){
                avatarId = user.getId()+"/profile/avatar";
                user.getProfile().setAvatarId(avatarId);
            }
            var uploadUrl = upload(request.getAvatar(), avatarId);
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
    public String upload(MultipartFile file, String url) throws IOException{
        var r = this.cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", url,
                        "overwrite ", true,
                        "resource_type", "auto"));
        return r.get("secure_url").toString();
    }
}

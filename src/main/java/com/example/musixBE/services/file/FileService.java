package com.example.musixBE.services.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, String url) throws IOException{
        var r = this.cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", url,
                        "overwrite ", true,
                        "resource_type", "auto"));
        return r.get("secure_url").toString();
    }
}

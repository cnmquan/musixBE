package com.example.musixBE.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, String url) throws IOException {
        var r = this.cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", url,
                        "overwrite ", true,
                        "resource_type", "auto"));
        return r.get("secure_url").toString();
    }

    public void destroy(String url, FileType type) throws IOException {
        this.cloudinary.uploader().destroy(url, ObjectUtils.asMap("public_id", url,
                "overwrite ", true,
                "resource_type", type.name()));
    }
}

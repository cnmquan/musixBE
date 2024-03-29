package com.example.musixBE.payloads.requests.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadProfileRequest {
    private String fullName;
    private String avatarUri;
    private String birthday;
    private String phoneNumber;
}

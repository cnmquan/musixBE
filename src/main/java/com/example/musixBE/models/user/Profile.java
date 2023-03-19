package com.example.musixBE.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String fullName;

    private String avatarUri;

    private String birthday;

    private String phoneNumber;

}

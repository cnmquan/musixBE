package com.example.musixBE.models.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDataDTO {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private boolean enable;
}

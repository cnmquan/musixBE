package com.example.musixBE.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    private boolean enabled;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProfileDTO profile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDTO> followings;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDTO> followers;

}

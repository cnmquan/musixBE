package com.example.musixBE.models.social;

import com.example.musixBE.models.user.User;
import com.example.musixBE.models.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportPostDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    PostDTO post;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String reason;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long dateCreated;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserDTO user;
}

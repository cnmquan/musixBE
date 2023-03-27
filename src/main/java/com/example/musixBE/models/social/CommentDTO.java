package com.example.musixBE.models.social;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ownerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ownerUsername;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> replies;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> likedBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long dateCreated;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lastModified;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isAuthor;
}

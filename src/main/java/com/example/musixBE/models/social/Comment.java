package com.example.musixBE.models.social;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("comments")
public class Comment {
    private String id;
    private String ownerId;
    private String ownerUsername;
    private List<String> replies;
    private List<String> likedBy;
    private Long dateCreated;
    private String content;
    private Long lastModified;
    private boolean isAuthor;
    private boolean isDeleted;
}

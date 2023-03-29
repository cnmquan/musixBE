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
@Document("posts")
public class Post {
    private String id;
    private String ownerId;
    private String ownerUsername;
    private String sourceId;
    private String thumbnailId;
    private String content;
    private String thumbnailUrl;
    private List<String> comments;
    private Long dateCreated;
    private Long lastModified;
    private List<String> likedBy;
    private String dataUrl;
}

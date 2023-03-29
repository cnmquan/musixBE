package com.example.musixBE.models.social;

import com.example.musixBE.utils.FileType;
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
    private String cloudId;
    private String ownerUsername;
    private String content;
    private List<String> comments;
    private FileType fileType;
    private Long dateCreated;
    private Long lastModified;
    private List<String> likedBy;
    private String dataUrl;
}

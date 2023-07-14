package com.example.musixBE.models.social;

import com.example.musixBE.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("report_post")
public class ReportPost {
    String id;
    Post post;
    String reason;
    Long dateCreated;
    User user;
}

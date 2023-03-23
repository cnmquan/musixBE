package com.example.musixBE.models.token;

import com.example.musixBE.models.user.User;
import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("tokens")
@ToString
public class Token {
    @Id
    private String id;

    @Field("token")
    @Indexed(unique = true)
    private String token;

    private TokenType tokenType;

    private boolean revoked;

    private boolean isExpired;

    private long dateExpired;

    private long dateCreated;
    @Nullable
    private long confirmedAt;
    // User save username, email, id
    @Field("user")
    private User user;
}
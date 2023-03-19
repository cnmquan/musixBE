package com.example.musixBE.models.token;

import com.example.musixBE.models.user.User;
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

    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean isExpired;

    private long dateExpired;

    private long dateCreated;

    // User save username, email, id
    @Field("user")
    private User user;
}
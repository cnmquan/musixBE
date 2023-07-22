package com.example.musixBE.services;

import com.example.musixBE.models.social.Post;
import com.example.musixBE.models.user.User;

public interface EmailSender {
    void sendVerificationLink(User user, String link);

    void sendResetPasswordEmail(User user, String token);

    void sendDeletedPostNotification(User user, Post post);

    void sendActivatedPostNotification(User user,Post post);
}

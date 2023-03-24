package com.example.musixBE.services;

import com.example.musixBE.models.user.User;

public interface EmailSender {
    void sendVerificationLink(User user, String link);
    void sendResetPasswordEmail(User user, String token);
}

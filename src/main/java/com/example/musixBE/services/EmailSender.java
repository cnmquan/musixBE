package com.example.musixBE.services;

import com.example.musixBE.models.user.User;

public interface EmailSender {
    void send(User user, String link);
}

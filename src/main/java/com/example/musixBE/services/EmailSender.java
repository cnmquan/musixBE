package com.example.musixBE.services;

import com.example.musixBE.models.user.User;

public interface EmailSender {
    void send(String to, User user, String link);
}

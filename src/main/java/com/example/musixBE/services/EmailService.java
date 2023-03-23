package com.example.musixBE.services;

import com.example.musixBE.models.user.User;
import com.example.musixBE.utils.EmailTemplateProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;


    @Override
    @Async
    public void send(User user, String link) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress("admin@musix.com"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Please confirm your email");

            message.setContent(EmailTemplateProvider.buildVerificationEmail(user.getUsername(), link), "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }
}

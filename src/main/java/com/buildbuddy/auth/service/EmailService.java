package com.buildbuddy.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${application.frontend.url}")
    private String frontendUrl;
    
    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("BuildBuddy - Email Verification");
        message.setText("Welcome to BuildBuddy!\n\n" +
                "Please verify your email by clicking the link below:\n" +
                frontendUrl + "/verify?token=" + token + "\n\n" +
                "Or use this API endpoint:\n" +
                "GET /api/auth/verify?token=" + token + "\n\n" +
                "If you didn't create an account, please ignore this email.");
        
        mailSender.send(message);
    }
}
package com.spdu.web.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String recipient, String title, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(recipient);
        mailMessage.setSubject(title);
        mailMessage.setText(body);

        javaMailSender.send(mailMessage);
    }
}

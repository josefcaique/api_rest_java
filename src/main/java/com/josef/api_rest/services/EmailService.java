package com.josef.api_rest.services;

import com.josef.api_rest.config.EmailConfig;
import com.josef.api_rest.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(String to, String subject, String body) {
        emailSender.to(to).withSubject(subject).withMessage(body).send(emailConfigs);
    }
}

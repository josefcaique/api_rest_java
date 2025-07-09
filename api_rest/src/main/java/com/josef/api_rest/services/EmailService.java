package com.josef.api_rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josef.api_rest.config.EmailConfig;
import com.josef.api_rest.data.dto.v1.request.EmailRequestDTO;
import com.josef.api_rest.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(EmailRequestDTO emailRequest) {
        String to = emailRequest.getTo();
        String subject = emailRequest.getSubject();
        String body = emailRequest.getBody();
        emailSender.to(to).withSubject(subject).withMessage(body).send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;

        try {
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            String to = emailRequest.getTo();
            String subject = emailRequest.getSubject();
            String body = emailRequest.getBody();
            emailSender.to(to).withSubject(subject).withMessage(body).attach(tempFile.getAbsoluteFile()).send(emailConfigs);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment", e);
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }

    }
}

package com.josef.api_rest.controllers;

import com.josef.api_rest.controllers.docs.EmailControllerDocs;
import com.josef.api_rest.data.dto.v1.request.EmailRequestDTO;
import com.josef.api_rest.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        service.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("e-mail sent with success", HttpStatus.OK);
    }


    @PostMapping(value = "/withAttachment")
    @Override
    public ResponseEntity<String> sendEmailWithAttachment
            (@RequestParam("emailRequest") String emailRequest,
             @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("email with attachment sent with success", HttpStatus.OK);
    }
}

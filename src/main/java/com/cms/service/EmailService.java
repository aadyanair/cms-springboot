package com.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    

    @Autowired
    private JavaMailSender mailSender;

    public void sendComplaintStatusUpdate(String toEMail, String subject, String message){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEMail);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);

    }
}

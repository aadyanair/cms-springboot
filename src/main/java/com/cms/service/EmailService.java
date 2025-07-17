package com.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendComplaintStatusUpdate(String toEMail, String subject, String message) {
        System.out.println("Preparing to send email...");
        System.out.println("To: " + toEMail);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toEMail);
            mail.setFrom("aadyanair49@gmail.com"); // optional but helps avoid issues
            mail.setSubject(subject);
            mail.setText(message);

            mailSender.send(mail);
            System.out.println("✅ Email sent successfully!");
        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.cms.controller;

import com.cms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestEmailController {
    

    @Autowired
    private EmailService emailService;

    @PostMapping("/api/test-email")
    public String sendTestEmail(@RequestParam String toEmail) {
        emailService.sendComplaintStatusUpdate(
            toEmail,
            "Test Email from Complaint Management System",
            "🎊 Your email setup is working perfectly!"
        );
        return "Email sent successfully to " + toEmail;
    }
}

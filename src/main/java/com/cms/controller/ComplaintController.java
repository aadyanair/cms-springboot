package com.cms.controller;


import com.cms.model.User;
import com.cms.model.Complaint;
import com.cms.repository.ComplaintRepository;
import com.cms.repository.UserRepository;
import com.cms.security.JwtUtil;
import com.cms.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Value("${admin.email}")
    private String adminEmail;

    // POST - Add a complaint
    @PostMapping
    public ResponseEntity<String> submitComplaint(@RequestBody Complaint complaint, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        complaint.setUsername(username);
        complaint.setStatus("PENDING");
        complaintRepository.save(complaint);

        // Send email to admin
        emailService.sendComplaintStatusUpdate(
            adminEmail,    
            "New Complaint Submitted",
            "A new complaint has been submitted by "+username+": \n\nTitle: "+complaint.getTitle()
        );
        return ResponseEntity.ok("Complaint submitted successfully");
    }

    // GET - All complaints submitted by this user
    @GetMapping
    public ResponseEntity<List<Complaint>> getMyComplaints(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        List<Complaint> complaints = complaintRepository.findByUsername(username);
        return ResponseEntity.ok(complaints);
    }


    // PUT - Update complaint status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, @RequestParam String status) {
        

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status);
        complaintRepository.save(complaint);

        // Get the user email via username in complaint
        String username = complaint.getUsername();
        User user = userRepository.findByUsername(username);

        if (user != null && user.getEmail() != null) {
            String subject = "Complaint Status Update";
            String body = "Dear " + username + ",\n\n"
                    + "Your complaint titled \"" + complaint.getTitle() + "\" has been marked as " + status + ".\n\n"
                    + "Regards,\nComplaint Management System";

            emailService.sendComplaintStatusUpdate(user.getEmail(), subject, body);
        }

        return ResponseEntity.ok("Complaint status updated and email sent.");
    }


}

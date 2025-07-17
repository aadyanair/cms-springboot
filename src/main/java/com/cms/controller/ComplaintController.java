package com.cms.controller;

import com.cms.model.Complaint;
import com.cms.repository.ComplaintRepository;
import com.cms.security.JwtUtil;
import com.cms.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            "aadyanair49@gmail.com",
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
}

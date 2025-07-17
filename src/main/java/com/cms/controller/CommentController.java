package com.cms.controller;

import com.cms.model.Comment;
import com.cms.model.Complaint;
import com.cms.repository.CommentRepository;
import com.cms.repository.ComplaintRepository;
import com.cms.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cms.model.User;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.cms.service.EmailService emailService;

    @Autowired
    private com.cms.repository.UserRepository userRepository;

    @PostMapping("/{complaintId}")
    public ResponseEntity<String> addComment(@PathVariable Long complaintId, @RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);

        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid complaint ID");
        }

        Complaint targetComplaint = complaintOpt.get();
        comment.setComplaint(targetComplaint);
        comment.setUsername(username);
        commentRepository.save(comment);

        // Notify complaint owner
        if (!targetComplaint.getUsername().equals(username)) {
            User user = userRepository.findByUsername(targetComplaint.getUsername());
            if (user != null) {
                emailService.sendComplaintStatusUpdate(
                    user.getEmail(),
                    "💬 New Comment on Your Complaint",
                    username + " commented on your complaint titled: " + targetComplaint.getTitle() + "\n\nComment: " + comment.getContent()
                );
            }
        }

        return ResponseEntity.ok("Comment added successfully");
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long complaintId) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Comment> comments = commentRepository.findByComplaint(complaintOpt.get());
        return ResponseEntity.ok(comments);
    }
}

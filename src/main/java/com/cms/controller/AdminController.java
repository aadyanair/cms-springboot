package com.cms.controller;

import com.cms.dto.ComplaintResponseDTO;
import com.cms.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // ✅ THIS IS REQUIRED
@RequestMapping("/api/admin") // ✅ So your endpoint becomes /api/admin/complaints
@RequiredArgsConstructor // ✅ Lombok will inject final ComplaintService
public class AdminController {

    private final ComplaintService complaintService;

    // GET /api/admin/complaints
    @GetMapping("/complaints")
    @PreAuthorize("hasRole('ADMIN')") // Only accessible by ADMINS
    public ResponseEntity<List<ComplaintResponseDTO>> getAllComplaints() {
        List<ComplaintResponseDTO> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/complaints/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateComplaintStatus(@PathVariable Long id,@RequestParam String status) {

        complaintService.updateStatus(id, status);
        return ResponseEntity.ok("Complaint status updated successfully.");
    }

    @GetMapping("/complaints/{id}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getCommentsForComplaint(@PathVariable Long id) {
        List<String> comments = complaintService.getCommentsByComplaintId(id);
        return ResponseEntity.ok(comments);
    }
}

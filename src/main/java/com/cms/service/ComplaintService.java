package com.cms.service;

import org.springframework.stereotype.Service;

import com.cms.dto.ComplaintResponseDTO;
import com.cms.model.Complaint;
import com.cms.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;

    public List<ComplaintResponseDTO> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();

        return complaints.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ComplaintResponseDTO mapToDTO(Complaint complaint) {
        return ComplaintResponseDTO.builder()
                .id(complaint.getId())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .category(complaint.getCategory().name())
                .createdAt(complaint.getCreatedAt())
                .userName(complaint.getUsername())
                .build();
    }

    public void updateStatus(Long complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status.toUpperCase()); // Example: PENDING, RESOLVED, REJECTED
        complaintRepository.save(complaint);
    }

    public List<String> getCommentsByComplaintId(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        return complaint.getComments().stream()
                .map(comment -> comment.getContent())
                .collect(Collectors.toList());
    }

}

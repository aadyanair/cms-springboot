package com.cms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ComplaintResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String category;
    private LocalDateTime createdAt;
    private String userName;
}

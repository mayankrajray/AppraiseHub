package com.appraisehub.dto;

import com.appraisehub.enums.AppraisalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalResponseDTO {
    private Long id;
    private Long employeeId;
    private Long reviewerId;
    private Long cycleId;
    private AppraisalStatus status;
    private Double finalScore;
    private String selfComments;
    private String managerComments;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
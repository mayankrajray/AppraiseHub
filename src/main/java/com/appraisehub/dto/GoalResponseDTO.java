package com.appraisehub.dto;

import com.appraisehub.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double weightage;
    private GoalStatus status;
    private Long userId;
    private Long cycleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

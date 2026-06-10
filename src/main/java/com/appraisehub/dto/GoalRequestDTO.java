package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequestDTO {
    private String title;
    private String description;
    private Double weightage;
    private Long userId;
    private Long cycleId;
}

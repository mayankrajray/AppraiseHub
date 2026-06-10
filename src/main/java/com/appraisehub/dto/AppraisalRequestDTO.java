package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalRequestDTO {
    private Long employeeId;
    private Long reviewerId;
    private Long cycleId;
    private String selfComments;
    private String managerComments;
    private Double finalScore;
}
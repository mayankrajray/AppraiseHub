package com.appraisehub.dto;

import com.appraisehub.entity.Goal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GoalResponseDTO {
    private Long id;
    private Long appraisalId;
    private Long employeeId;
    private String employeeName;
    private String title;
    private String description;
    private Goal.Status status;
    private LocalDate dueDate;
}
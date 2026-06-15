package com.appraisehub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GoalRequestDTO {
    private Long appraisalId;
    private String title;
    private String description;
    private LocalDate dueDate;
}
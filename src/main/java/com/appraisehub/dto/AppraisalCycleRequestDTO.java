package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCycleRequestDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdBy;
}

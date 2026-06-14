package com.appraisehub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAppraisalRequestDTO {
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private Long employeeId;
    private Long managerId;
}
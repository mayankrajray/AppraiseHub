package com.appraisehub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BulkCycleRequestDTO {
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private Long departmentId;
}
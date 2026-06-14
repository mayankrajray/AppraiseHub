package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BulkCycleResponseDTO {
    private String cycleName;
    private int totalEmployees;
    private int created;
    private int skippedAlreadyExists;
    private int skippedNoManager;
}
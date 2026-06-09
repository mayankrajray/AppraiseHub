package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role;
    private Long departmentId;
}

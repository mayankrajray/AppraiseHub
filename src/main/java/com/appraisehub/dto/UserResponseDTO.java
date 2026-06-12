package com.appraisehub.dto;

import com.appraisehub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String jobTitle;
    private boolean isActive;
    private Long departmentId;
    private String departmentName;
    private Long managerId;
    private String managerName;
    private LocalDateTime createdAt;
}

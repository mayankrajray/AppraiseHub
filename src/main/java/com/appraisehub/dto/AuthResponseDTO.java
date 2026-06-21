package com.appraisehub.dto;

import com.appraisehub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
    private String jobTitle;
    private String departmentName;
    private Long managerId;
    private String managerName;
}
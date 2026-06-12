package com.appraisehub.dto;

import com.appraisehub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private Role role;
    private String jobTitle;
    private Long departmentId;
    private Long managerId;
}

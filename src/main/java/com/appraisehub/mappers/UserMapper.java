package com.appraisehub.mappers;

import com.appraisehub.dto.UserResponseDTO;
import com.appraisehub.entity.User;

public class UserMapper {

    public static UserResponseDTO toResponse(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setJobTitle(user.getJobTitle());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getDepartment() != null) {
            dto.setDepartmentId(user.getDepartment().getId());
            dto.setDepartmentName(user.getDepartment().getName());
        }

        if (user.getManager() != null) {
            dto.setManagerId(user.getManager().getId());
            dto.setManagerName(user.getManager().getFullName());
        }

        return dto;
    }
}
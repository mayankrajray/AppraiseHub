package com.appraisehub.mappers;

import com.appraisehub.dto.DepartmentResponseDTO;
import com.appraisehub.entity.Department;

public class DepartmentMapper {

    public static DepartmentResponseDTO toResponse(Department department) {
        if (department == null) return null;

        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setUserCount(department.getUsers().size());

        return dto;
    }
}
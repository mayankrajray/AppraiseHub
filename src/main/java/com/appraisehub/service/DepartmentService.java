package com.appraisehub.service;

import com.appraisehub.dto.DepartmentRequestDTO;
import com.appraisehub.dto.DepartmentResponseDTO;


import java.util.List;

public interface DepartmentService {

    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO getDepartmentById(Long id);
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);
    DepartmentResponseDTO updateDepartment (Long id, DepartmentRequestDTO requestDTO);
    void deleteDepartment(Long id);
}

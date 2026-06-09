package com.appraisehub.service;

import com.appraisehub.dto.EmployeeRequestDTO;
import com.appraisehub.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO getEmployeeById(Long id);
    EmployeeResponseDTO addEmployee (EmployeeRequestDTO requestDTO);
    EmployeeResponseDTO updateEmployee (Long id, EmployeeRequestDTO requestDTO);
    void deleteEmployee(Long id);
}

package com.appraisehub.service.impl;

import com.appraisehub.dto.DepartmentRequestDTO;
import com.appraisehub.dto.DepartmentResponseDTO;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.model.Department;
import com.appraisehub.repository.DepartmentRepository;
import com.appraisehub.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    private DepartmentResponseDTO convertToResponseDTO(Department department) {
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(department.getId());
        responseDTO.setName(department.getName());
        responseDTO.setManagerId(department.getManagerId());
        return responseDTO;
    }

    private Department convertToEntity(DepartmentRequestDTO requestDTO) {
        Department department = new Department();
        department.setName(requestDTO.getName());
        department.setManagerId(requestDTO.getManagerId());
        return department;
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));
        return convertToResponseDTO(department);
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        Department department = convertToEntity(requestDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToResponseDTO(savedDepartment);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));
        existing.setName(requestDTO.getName());
        existing.setManagerId(requestDTO.getManagerId());
        Department updatedDepartment = departmentRepository.save(existing);
        return convertToResponseDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id));
        departmentRepository.deleteById(id);
    }
}
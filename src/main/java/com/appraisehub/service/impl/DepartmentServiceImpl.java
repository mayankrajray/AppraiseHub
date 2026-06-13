package com.appraisehub.service.impl;

import com.appraisehub.dto.DepartmentRequestDTO;
import com.appraisehub.dto.DepartmentResponseDTO;
import com.appraisehub.entity.Department;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.mappers.DepartmentMapper;
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

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department", id));
        return DepartmentMapper.toResponse(department);
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        Department department = Department.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.toResponse(savedDepartment);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department", id));
        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        Department updatedDepartment = departmentRepository.save(existing);
        return DepartmentMapper.toResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department", id));
        departmentRepository.deleteById(id);
    }
}
package com.appraisehub.service.impl;

import com.appraisehub.dto.EmployeeRequestDTO;
import com.appraisehub.dto.EmployeeResponseDTO;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.model.Department;
import com.appraisehub.model.Employee;
import com.appraisehub.repository.EmployeeRepository;
import com.appraisehub.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appraisehub.repository.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee convertToEntity(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee();
        employee.setName(requestDTO.getName());
        employee.setEmail(requestDTO.getEmail());
        employee.setRole(requestDTO.getRole());

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department not found with id: " + requestDTO.getDepartmentId()));
            employee.setDepartment(department);
        }
        return employee;
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(employee.getId());
        responseDTO.setName(employee.getName());
        responseDTO.setEmail(employee.getEmail());
        responseDTO.setRole(employee.getRole());

        if (employee.getDepartment() != null) {
            responseDTO.setDepartmentId(employee.getDepartment().getId());
            responseDTO.setDepartmentName(employee.getDepartment().getName());
        }
        return responseDTO;
    }


    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id));
        return convertToResponseDTO(employee);
    }


    @Override
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = convertToEntity(requestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id));

        existing.setName(requestDTO.getName());
        existing.setEmail(requestDTO.getEmail());
        existing.setRole(requestDTO.getRole());

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department not found with id: " + requestDTO.getDepartmentId()));
            existing.setDepartment(department);
        }

        Employee updatedEmployee = employeeRepository.save(existing);
        return convertToResponseDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.deleteById(id);
    }
}

package com.appraisehub.controller;

import com.appraisehub.dto.ApiResponse;
import com.appraisehub.dto.DepartmentRequestDTO;
import com.appraisehub.dto.DepartmentResponseDTO;
import com.appraisehub.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(
            @PathVariable Long id) {
        DepartmentResponseDTO department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(
            @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO savedDepartment = departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", savedDepartment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(
                ApiResponse.success("Department updated successfully", updatedDepartment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(
                ApiResponse.success("Department deleted successfully", null));
    }
}
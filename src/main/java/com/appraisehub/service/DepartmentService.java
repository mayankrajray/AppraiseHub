package com.appraisehub.service;

import com.appraisehub.model.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department createDepartment(Department department);
    Department updateDepartment (Long id, Department department);
    void deleteDepartment(Long id);
}

package com.appraisehub.service;

import com.appraisehub.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee addEmployee (Employee employee);
    Employee updateEmployee (Long id, Employee employee);
    void deleteEmployee(Long id);
}

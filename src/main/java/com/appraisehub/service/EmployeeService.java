package com.appraisehub.service;

import com.appraisehub.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();
    Employee getEmployeeById(Integer id);
    Employee addEmployee (Employee employee);
    Employee updateEmployee (Integer id, Employee employee);
    void deleteEmployee(Integer id);
}

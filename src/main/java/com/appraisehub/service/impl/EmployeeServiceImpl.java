package com.appraisehub.service.impl;

import com.appraisehub.model.Employee;
import com.appraisehub.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private List<Employee> employees = new ArrayList<>();
    private int idCounter = 1;


    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        for(Employee employee: employees){
            if (employee.getId().equals(id)){
                return employee;
            }
        }
        return null;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        employee.setId(idCounter);
        employees.add(employee);
        idCounter++;
        return employee;
    }

    @Override
    public Employee updateEmployee(Integer id, Employee employee) {
        for (Employee emp : employees) {
            if (emp.getId().equals(id)) {
                emp.setName(employee.getName());
                emp.setEmail(employee.getEmail());
                emp.setDepartment(employee.getDepartment());
                emp.setRole(employee.getRole());
                return emp;
            }
        }
        return null;
    }

    @Override
    public void deleteEmployee(Integer id) {
        employees.removeIf(employee -> employee.getId().equals(id));
    }
}

package com.appraisehub.model;

public class Employee {
    private Integer id;
    private String name;
    private String email;
    private String department;
    private String role;

    public Employee(Integer id, String name, String email, String department, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    public Employee(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

package com.appraisehub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {
    private Integer id;
    private String name;
    private String email;
    private String department;
    private String role;

}

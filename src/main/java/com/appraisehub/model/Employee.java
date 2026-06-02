package com.appraisehub.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Employee {
    private Integer id;
    private String name;
    private String email;
    private String department;
    private String role;

}

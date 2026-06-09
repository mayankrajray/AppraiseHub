package com.appraisehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
   private Long id;
   private String name;
   private String email;
   private String role;
   private Long departmentId;
   private String departmentName;
}

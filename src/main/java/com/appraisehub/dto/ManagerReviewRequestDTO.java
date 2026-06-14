package com.appraisehub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerReviewRequestDTO {
    private String managerStrengths;
    private String managerImprovements;
    private String managerComments;
    private Integer managerRating;
}
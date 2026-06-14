package com.appraisehub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfAssessmentRequestDTO {
    private String whatWentWell;
    private String whatToImprove;
    private String achievements;
    private Integer selfRating;
}
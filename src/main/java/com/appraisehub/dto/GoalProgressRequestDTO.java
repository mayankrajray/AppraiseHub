package com.appraisehub.dto;

import com.appraisehub.entity.Goal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalProgressRequestDTO {
    private Goal.Status status;
}
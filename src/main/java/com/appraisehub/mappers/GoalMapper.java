package com.appraisehub.mappers;

import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.entity.Goal;

public final class GoalMapper {

    private GoalMapper() {
    }

    public static GoalResponseDTO toResponse(Goal goal) {
        GoalResponseDTO response = new GoalResponseDTO();
        response.setId(goal.getId());
        response.setAppraisalId(goal.getAppraisal().getId());
        response.setEmployeeId(goal.getEmployee().getId());
        response.setEmployeeName(goal.getEmployee().getFullName());
        response.setTitle(goal.getTitle());
        response.setDescription(goal.getDescription());
        response.setStatus(goal.getStatus());
        response.setDueDate(goal.getDueDate());
        return response;
    }
}
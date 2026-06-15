package com.appraisehub.service;

import com.appraisehub.dto.GoalProgressRequestDTO;
import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;

import java.util.List;

public interface GoalService {

    GoalResponseDTO createGoal(GoalRequestDTO request, Long managerId);

    GoalResponseDTO getGoalById(Long goalId);

    List<GoalResponseDTO> getGoalsByAppraisal(Long appraisalId);

    List<GoalResponseDTO> getGoalsByEmployee(Long employeeId);

    GoalResponseDTO updateGoal(Long goalId, GoalRequestDTO request, Long managerId);

    GoalResponseDTO updateProgress(Long goalId, GoalProgressRequestDTO request, Long employeeId);

    void deleteGoal(Long goalId, Long managerId);
}
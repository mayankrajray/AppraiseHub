package com.appraisehub.service;

import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.enums.GoalStatus;

import java.util.List;

public interface GoalService {
    List<GoalResponseDTO> getAllGoals();
    GoalResponseDTO getGoalById(Long id);
    GoalResponseDTO createGoal(GoalRequestDTO requestDTO);
    GoalResponseDTO updateGoal(Long id, GoalRequestDTO requestDTO);
    void deleteGoal(Long id);
    List<GoalResponseDTO> getGoalsByUserId(Long userId);
    List<GoalResponseDTO> getGoalsByCycleId(Long cycleId);
    List<GoalResponseDTO> getGoalsByUserIdAndCycleId(Long userId, Long cycleId);
    GoalResponseDTO updateGoalStatus(Long id, GoalStatus status);
}

package com.appraisehub.service.impl;

import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.enums.GoalStatus;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.model.Goal;
import com.appraisehub.repository.GoalRepository;
import com.appraisehub.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository goalRepository;

    private GoalResponseDTO convertToResponseDTO(Goal goal) {
        GoalResponseDTO responseDTO = new GoalResponseDTO();
        responseDTO.setId(goal.getId());
        responseDTO.setTitle(goal.getTitle());
        responseDTO.setDescription(goal.getDescription());
        responseDTO.setWeightage(goal.getWeightage());
        responseDTO.setStatus(goal.getStatus());
        responseDTO.setUserId(goal.getUserId());
        responseDTO.setCycleId(goal.getCycleId());
        responseDTO.setCreatedAt(goal.getCreatedAt());
        responseDTO.setUpdatedAt(goal.getUpdatedAt());
        return responseDTO;
    }

    private Goal convertToEntity(GoalRequestDTO requestDTO) {
        Goal goal = new Goal();
        goal.setTitle(requestDTO.getTitle());
        goal.setDescription(requestDTO.getDescription());
        goal.setWeightage(requestDTO.getWeightage());
        goal.setUserId(requestDTO.getUserId());
        goal.setCycleId(requestDTO.getCycleId());
        return goal;
    }

    @Override
    public List<GoalResponseDTO> getAllGoals() {
        return goalRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GoalResponseDTO getGoalById(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + id));
        return convertToResponseDTO(goal);
    }

    @Override
    public GoalResponseDTO createGoal(GoalRequestDTO requestDTO) {
        Goal goal = convertToEntity(requestDTO);
        Goal savedGoal = goalRepository.save(goal);
        return convertToResponseDTO(savedGoal);
    }

    @Override
    public GoalResponseDTO updateGoal(Long id, GoalRequestDTO requestDTO) {
        Goal existing = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + id));
        existing.setTitle(requestDTO.getTitle());
        existing.setDescription(requestDTO.getDescription());
        existing.setWeightage(requestDTO.getWeightage());
        existing.setUserId(requestDTO.getUserId());
        existing.setCycleId(requestDTO.getCycleId());
        Goal updatedGoal = goalRepository.save(existing);
        return convertToResponseDTO(updatedGoal);
    }

    @Override
    public void deleteGoal(Long id) {
        goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + id));
        goalRepository.deleteById(id);
    }

    @Override
    public List<GoalResponseDTO> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalResponseDTO> getGoalsByCycleId(Long cycleId) {
        return goalRepository.findByCycleId(cycleId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalResponseDTO> getGoalsByUserIdAndCycleId(Long userId, Long cycleId) {
        return goalRepository.findByUserIdAndCycleId(userId, cycleId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GoalResponseDTO updateGoalStatus(Long id, GoalStatus status) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + id));
        goal.setStatus(status);
        Goal updatedGoal = goalRepository.save(goal);
        return convertToResponseDTO(updatedGoal);
    }
}
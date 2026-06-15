package com.appraisehub.service.impl;

import com.appraisehub.dto.GoalProgressRequestDTO;
import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.entity.Appraisal;
import com.appraisehub.entity.Goal;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.exception.UnauthorizedAccessException;
import com.appraisehub.mappers.GoalMapper;
import com.appraisehub.repository.AppraisalRepository;
import com.appraisehub.repository.GoalRepository;
import com.appraisehub.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private AppraisalRepository appraisalRepository;

    @Override
    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO request, Long managerId) {
        if (request.getAppraisalId() == null) {
            throw new IllegalArgumentException("Appraisal ID is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Goal title is required");
        }

        Appraisal appraisal = appraisalRepository.findByIdWithDetails(
                        request.getAppraisalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal", request.getAppraisalId()));

        if (!appraisal.getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: you are not the manager for this appraisal");
        }

        Goal goal = Goal.builder()
                .appraisal(appraisal)
                .employee(appraisal.getEmployee())
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .build();

        goalRepository.save(goal);
        return GoalMapper.toResponse(goal);
    }

    @Override
    @Transactional(readOnly = true)
    public GoalResponseDTO getGoalById(Long goalId) {
        Goal goal = findById(goalId);
        return GoalMapper.toResponse(goal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> getGoalsByAppraisal(Long appraisalId) {
        return goalRepository.findByAppraisalId(appraisalId)
                .stream()
                .map(GoalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> getGoalsByEmployee(Long employeeId) {
        return goalRepository.findByEmployeeId(employeeId)
                .stream()
                .map(GoalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoal(Long goalId, GoalRequestDTO request,
                                      Long managerId) {
        Goal goal = findById(goalId);

        if (!goal.getAppraisal().getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: only the manager can update this goal");
        }

        if (request.getTitle() != null) goal.setTitle(request.getTitle());
        if (request.getDescription() != null) goal.setDescription(request.getDescription());
        if (request.getDueDate() != null) goal.setDueDate(request.getDueDate());

        goalRepository.save(goal);
        return GoalMapper.toResponse(goal);
    }

    @Override
    @Transactional
    public GoalResponseDTO updateProgress(Long goalId,
                                          GoalProgressRequestDTO request, Long employeeId) {
        Goal goal = findById(goalId);

        if (!goal.getEmployee().getId().equals(employeeId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: this is not your goal");
        }

        goal.setStatus(request.getStatus());
        goalRepository.save(goal);
        return GoalMapper.toResponse(goal);
    }

    @Override
    @Transactional
    public void deleteGoal(Long goalId, Long managerId) {
        Goal goal = findById(goalId);

        if (!goal.getAppraisal().getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: only the manager can delete this goal");
        }

        goalRepository.delete(goal);
    }

    private Goal findById(Long id) {
        return goalRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", id));
    }
}
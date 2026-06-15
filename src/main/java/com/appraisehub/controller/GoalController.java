package com.appraisehub.controller;

import com.appraisehub.dto.ApiResponse;
import com.appraisehub.dto.GoalProgressRequestDTO;
import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public ResponseEntity<ApiResponse<GoalResponseDTO>> createGoal(
            @RequestBody GoalRequestDTO request,
            @RequestParam Long managerId) {
        GoalResponseDTO response = goalService.createGoal(request, managerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Goal created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponseDTO>> getGoalById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(goalService.getGoalById(id)));
    }

    @GetMapping("/appraisal/{appraisalId}")
    public ResponseEntity<ApiResponse<List<GoalResponseDTO>>> getGoalsByAppraisal(
            @PathVariable Long appraisalId) {
        return ResponseEntity.ok(
                ApiResponse.success(goalService.getGoalsByAppraisal(appraisalId)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<GoalResponseDTO>>> getGoalsByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(
                ApiResponse.success(goalService.getGoalsByEmployee(employeeId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponseDTO>> updateGoal(
            @PathVariable Long id,
            @RequestBody GoalRequestDTO request,
            @RequestParam Long managerId) {
        GoalResponseDTO response = goalService.updateGoal(id, request, managerId);
        return ResponseEntity.ok(
                ApiResponse.success("Goal updated successfully", response));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<GoalResponseDTO>> updateProgress(
            @PathVariable Long id,
            @RequestBody GoalProgressRequestDTO request,
            @RequestParam Long employeeId) {
        GoalResponseDTO response = goalService.updateProgress(id, request, employeeId);
        return ResponseEntity.ok(
                ApiResponse.success("Goal progress updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGoal(
            @PathVariable Long id,
            @RequestParam Long managerId) {
        goalService.deleteGoal(id, managerId);
        return ResponseEntity.ok(
                ApiResponse.success("Goal deleted successfully", null));
    }
}
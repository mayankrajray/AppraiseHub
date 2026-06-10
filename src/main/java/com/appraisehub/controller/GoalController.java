package com.appraisehub.controller;

import com.appraisehub.dto.GoalRequestDTO;
import com.appraisehub.dto.GoalResponseDTO;
import com.appraisehub.enums.GoalStatus;
import com.appraisehub.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        List<GoalResponseDTO> goals = goalService.getAllGoals();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> getGoalById(@PathVariable Long id) {
        GoalResponseDTO goal = goalService.getGoalById(id);
        return ResponseEntity.ok(goal);
    }

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@RequestBody GoalRequestDTO requestDTO) {
        GoalResponseDTO savedGoal = goalService.createGoal(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGoal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponseDTO> updateGoal(@PathVariable Long id, @RequestBody GoalRequestDTO requestDTO) {
        GoalResponseDTO updatedGoal = goalService.updateGoal(id, requestDTO);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByUserId(@PathVariable Long userId) {
        List<GoalResponseDTO> goals = goalService.getGoalsByUserId(userId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/cycle/{cycleId}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByCycleId(@PathVariable Long cycleId) {
        List<GoalResponseDTO> goals = goalService.getGoalsByCycleId(cycleId);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/user/{userId}/cycle/{cycleId}")
    public ResponseEntity<List<GoalResponseDTO>> getGoalsByUserIdAndCycleId(
            @PathVariable Long userId,
            @PathVariable Long cycleId) {
        List<GoalResponseDTO> goals = goalService.getGoalsByUserIdAndCycleId(userId, cycleId);
        return ResponseEntity.ok(goals);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GoalResponseDTO> updateGoalStatus(
            @PathVariable Long id,
            @RequestParam GoalStatus status) {
        GoalResponseDTO goal = goalService.updateGoalStatus(id, status);
        return ResponseEntity.ok(goal);
    }
}
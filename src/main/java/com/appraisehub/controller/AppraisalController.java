package com.appraisehub.controller;

import com.appraisehub.dto.AppraisalRequestDTO;
import com.appraisehub.dto.AppraisalResponseDTO;
import com.appraisehub.enums.AppraisalStatus;
import com.appraisehub.service.AppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appraisals")
public class AppraisalController {

    @Autowired
    private AppraisalService appraisalService;

    @GetMapping
    public ResponseEntity<List<AppraisalResponseDTO>> getAllAppraisals() {
        List<AppraisalResponseDTO> appraisals = appraisalService.getAllAppraisals();
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalResponseDTO> getAppraisalById(@PathVariable Long id) {
        AppraisalResponseDTO appraisal = appraisalService.getAppraisalById(id);
        return ResponseEntity.ok(appraisal);
    }

    @PostMapping
    public ResponseEntity<AppraisalResponseDTO> createAppraisal(@RequestBody AppraisalRequestDTO requestDTO) {
        AppraisalResponseDTO savedAppraisal = appraisalService.createAppraisal(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppraisal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppraisalResponseDTO> updateAppraisal(@PathVariable Long id, @RequestBody AppraisalRequestDTO requestDTO) {
        AppraisalResponseDTO updatedAppraisal = appraisalService.updateAppraisal(id, requestDTO);
        return ResponseEntity.ok(updatedAppraisal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppraisal(@PathVariable Long id) {
        appraisalService.deleteAppraisal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AppraisalResponseDTO>> getAppraisalsByEmployeeId(@PathVariable Long employeeId) {
        List<AppraisalResponseDTO> appraisals = appraisalService.getAppraisalsByEmployeeId(employeeId);
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("/cycle/{cycleId}")
    public ResponseEntity<List<AppraisalResponseDTO>> getAppraisalsByCycleId(@PathVariable Long cycleId) {
        List<AppraisalResponseDTO> appraisals = appraisalService.getAppraisalsByCycleId(cycleId);
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<AppraisalResponseDTO>> getAppraisalsByReviewerId(@PathVariable Long reviewerId) {
        List<AppraisalResponseDTO> appraisals = appraisalService.getAppraisalsByReviewerId(reviewerId);
        return ResponseEntity.ok(appraisals);
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<AppraisalResponseDTO> submitAppraisal(@PathVariable Long id) {
        AppraisalResponseDTO appraisal = appraisalService.submitAppraisal(id);
        return ResponseEntity.ok(appraisal);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppraisalResponseDTO> updateAppraisalStatus(
            @PathVariable Long id,
            @RequestParam AppraisalStatus status) {
        AppraisalResponseDTO appraisal = appraisalService.updateAppraisalStatus(id, status);
        return ResponseEntity.ok(appraisal);
    }
}
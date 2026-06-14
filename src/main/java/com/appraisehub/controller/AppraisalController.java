package com.appraisehub.controller;

import com.appraisehub.dto.*;
import com.appraisehub.service.AppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appraisals")
public class AppraisalController {

    @Autowired
    private AppraisalService appraisalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppraisalResponseDTO>>> getAllAppraisals() {
        return ResponseEntity.ok(
                ApiResponse.success(appraisalService.getAllAppraisals()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> createAppraisal(
            @RequestBody CreateAppraisalRequestDTO request) {
        AppraisalResponseDTO response = appraisalService.createAppraisal(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appraisal created successfully", response));
    }

    @PostMapping("/cycle/bulk-create")
    public ResponseEntity<ApiResponse<BulkCycleResponseDTO>> createBulkCycle(
            @RequestBody BulkCycleRequestDTO request) {
        BulkCycleResponseDTO response = appraisalService.createBulkCycle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bulk cycle created", response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<AppraisalResponseDTO>>> getMyAppraisals(
            @RequestParam Long employeeId) {
        return ResponseEntity.ok(
                ApiResponse.success(appraisalService.getMyAppraisals(employeeId)));
    }

    @GetMapping("/team")
    public ResponseEntity<ApiResponse<List<AppraisalResponseDTO>>> getTeamAppraisals(
            @RequestParam Long managerId) {
        return ResponseEntity.ok(
                ApiResponse.success(appraisalService.getTeamAppraisals(managerId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> getAppraisalById(
            @PathVariable Long id,
            @RequestParam Long requesterId) {
        return ResponseEntity.ok(
                ApiResponse.success(appraisalService.getAppraisalById(id, requesterId)));
    }

    @PutMapping("/{id}/self-assessment/draft")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> saveSelfAssessmentDraft(
            @PathVariable Long id,
            @RequestBody SelfAssessmentRequestDTO request,
            @RequestParam Long employeeId) {
        AppraisalResponseDTO response =
                appraisalService.saveSelfAssessmentDraft(id, request, employeeId);
        return ResponseEntity.ok(ApiResponse.success("Draft saved", response));
    }

    @PutMapping("/{id}/self-assessment/submit")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> submitSelfAssessment(
            @PathVariable Long id,
            @RequestBody SelfAssessmentRequestDTO request,
            @RequestParam Long employeeId) {
        AppraisalResponseDTO response =
                appraisalService.submitSelfAssessment(id, request, employeeId);
        return ResponseEntity.ok(
                ApiResponse.success("Self-assessment submitted", response));
    }

    @PutMapping("/{id}/manager-review/draft")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> saveManagerReviewDraft(
            @PathVariable Long id,
            @RequestBody ManagerReviewRequestDTO request,
            @RequestParam Long managerId) {
        AppraisalResponseDTO response =
                appraisalService.saveManagerReviewDraft(id, request, managerId);
        return ResponseEntity.ok(ApiResponse.success("Review draft saved", response));
    }

    @PutMapping("/{id}/manager-review/submit")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> submitManagerReview(
            @PathVariable Long id,
            @RequestBody ManagerReviewRequestDTO request,
            @RequestParam Long managerId) {
        AppraisalResponseDTO response =
                appraisalService.submitManagerReview(id, request, managerId);
        return ResponseEntity.ok(
                ApiResponse.success("Manager review submitted", response));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> approveAppraisal(
            @PathVariable Long id) {
        AppraisalResponseDTO response = appraisalService.approveAppraisal(id);
        return ResponseEntity.ok(ApiResponse.success("Appraisal approved", response));
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<ApiResponse<AppraisalResponseDTO>> acknowledgeAppraisal(
            @PathVariable Long id,
            @RequestParam Long employeeId) {
        AppraisalResponseDTO response =
                appraisalService.acknowledgeAppraisal(id, employeeId);
        return ResponseEntity.ok(
                ApiResponse.success("Appraisal acknowledged", response));
    }
}
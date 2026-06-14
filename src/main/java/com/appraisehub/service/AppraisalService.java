package com.appraisehub.service;

import com.appraisehub.dto.*;

import java.util.List;

public interface AppraisalService {

    AppraisalResponseDTO createAppraisal(CreateAppraisalRequestDTO request);

    List<AppraisalResponseDTO> getAllAppraisals();

    BulkCycleResponseDTO createBulkCycle(BulkCycleRequestDTO request);

    List<AppraisalResponseDTO> getMyAppraisals(Long employeeId);

    List<AppraisalResponseDTO> getTeamAppraisals(Long managerId);

    AppraisalResponseDTO getAppraisalById(Long appraisalId, Long requesterId);

    AppraisalResponseDTO saveSelfAssessmentDraft(Long appraisalId,SelfAssessmentRequestDTO request, Long employeeId);

    AppraisalResponseDTO submitSelfAssessment(Long appraisalId,SelfAssessmentRequestDTO request, Long employeeId);

    AppraisalResponseDTO saveManagerReviewDraft(Long appraisalId,ManagerReviewRequestDTO request, Long managerId);

    AppraisalResponseDTO submitManagerReview(Long appraisalId,ManagerReviewRequestDTO request, Long managerId);

    AppraisalResponseDTO approveAppraisal(Long appraisalId);

    AppraisalResponseDTO acknowledgeAppraisal(Long appraisalId, Long employeeId);
}
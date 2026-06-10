package com.appraisehub.service;

import com.appraisehub.dto.AppraisalRequestDTO;
import com.appraisehub.dto.AppraisalResponseDTO;
import com.appraisehub.enums.AppraisalStatus;

import java.util.List;

public interface AppraisalService {
    List<AppraisalResponseDTO> getAllAppraisals();
    AppraisalResponseDTO getAppraisalById(Long id);
    AppraisalResponseDTO createAppraisal(AppraisalRequestDTO requestDTO);
    AppraisalResponseDTO updateAppraisal(Long id, AppraisalRequestDTO requestDTO);
    void deleteAppraisal(Long id);
    List<AppraisalResponseDTO> getAppraisalsByEmployeeId(Long employeeId);
    List<AppraisalResponseDTO> getAppraisalsByCycleId(Long cycleId);
    List<AppraisalResponseDTO> getAppraisalsByReviewerId(Long reviewerId);
    AppraisalResponseDTO submitAppraisal(Long id);
    AppraisalResponseDTO updateAppraisalStatus(Long id, AppraisalStatus status);
}
package com.appraisehub.service;

import com.appraisehub.dto.AppraisalCycleRequestDTO;
import com.appraisehub.dto.AppraisalCycleResponseDTO;

import java.util.List;

public interface AppraisalCycleService {
    List<AppraisalCycleResponseDTO> getAllCycles();
    AppraisalCycleResponseDTO getCycleById(Long id);
    AppraisalCycleResponseDTO createCycle(AppraisalCycleRequestDTO requestDTO);
    AppraisalCycleResponseDTO updateCycle(Long id, AppraisalCycleRequestDTO requestDTO);
    void deleteCycle(Long id);
    AppraisalCycleResponseDTO activateCycle(Long id);
    AppraisalCycleResponseDTO closeCycle(Long id);
}

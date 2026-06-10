package com.appraisehub.service.impl;

import com.appraisehub.dto.AppraisalCycleRequestDTO;
import com.appraisehub.dto.AppraisalCycleResponseDTO;
import com.appraisehub.enums.CycleStatus;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.model.AppraisalCycle;
import com.appraisehub.repository.AppraisalCycleRepository;
import com.appraisehub.service.AppraisalCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppraisalCycleServiceImpl implements AppraisalCycleService {

    @Autowired
    private AppraisalCycleRepository appraisalCycleRepository;

    //convertToEntity
    private AppraisalCycle convertToEntity(AppraisalCycleRequestDTO requestDTO) {
        AppraisalCycle cycle = new AppraisalCycle();
        cycle.setName(requestDTO.getName());
        cycle.setStartDate(requestDTO.getStartDate());
        cycle.setEndDate(requestDTO.getEndDate());
        cycle.setCreatedBy(requestDTO.getCreatedBy());
        return cycle;
    }

    // convertToResponseDTO
    private AppraisalCycleResponseDTO convertToResponseDTO(AppraisalCycle cycle) {
        AppraisalCycleResponseDTO responseDTO = new AppraisalCycleResponseDTO();
        responseDTO.setId(cycle.getId());
        responseDTO.setName(cycle.getName());
        responseDTO.setStartDate(cycle.getStartDate());
        responseDTO.setEndDate(cycle.getEndDate());
        responseDTO.setStatus(cycle.getStatus());
        responseDTO.setCreatedBy(cycle.getCreatedBy());
        responseDTO.setCreatedAt(cycle.getCreatedAt());
        responseDTO.setUpdatedAt(cycle.getUpdatedAt());
        return responseDTO;
    }

    @Override
    public List<AppraisalCycleResponseDTO> getAllCycles() {
        return appraisalCycleRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppraisalCycleResponseDTO getCycleById(Long id) {
        AppraisalCycle cycle = appraisalCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal Cycle not found with id: " + id));
        return convertToResponseDTO(cycle);
    }

    @Override
    public AppraisalCycleResponseDTO createCycle(AppraisalCycleRequestDTO requestDTO) {
        AppraisalCycle cycle = convertToEntity(requestDTO);
        AppraisalCycle savedCycle = appraisalCycleRepository.save(cycle);
        return convertToResponseDTO(savedCycle);
    }

    @Override
    public AppraisalCycleResponseDTO updateCycle(Long id, AppraisalCycleRequestDTO requestDTO) {
        AppraisalCycle existing = appraisalCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal Cycle not found with id: " + id));
        existing.setName(requestDTO.getName());
        existing.setStartDate(requestDTO.getStartDate());
        existing.setEndDate(requestDTO.getEndDate());
        existing.setCreatedBy(requestDTO.getCreatedBy());
        AppraisalCycle updatedCycle = appraisalCycleRepository.save(existing);
        return convertToResponseDTO(updatedCycle);
    }

    @Override
    public void deleteCycle(Long id) {
        appraisalCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal Cycle not found with id: " + id));
        appraisalCycleRepository.deleteById(id);
    }

    @Override
    public AppraisalCycleResponseDTO activateCycle(Long id) {
        AppraisalCycle cycle = appraisalCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal Cycle not found with id: " + id));
        cycle.setStatus(CycleStatus.ACTIVE);
        AppraisalCycle updatedCycle = appraisalCycleRepository.save(cycle);
        return convertToResponseDTO(updatedCycle);
    }

    @Override
    public AppraisalCycleResponseDTO closeCycle(Long id) {
        AppraisalCycle cycle = appraisalCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal Cycle not found with id: " + id));
        cycle.setStatus(CycleStatus.CLOSED);
        AppraisalCycle updatedCycle = appraisalCycleRepository.save(cycle);
        return convertToResponseDTO(updatedCycle);
    }


}

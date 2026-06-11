package com.appraisehub.service.impl;

import com.appraisehub.dto.AppraisalRequestDTO;
import com.appraisehub.dto.AppraisalResponseDTO;
import com.appraisehub.enums.AppraisalStatus;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.entity.Appraisal;
import com.appraisehub.repository.AppraisalRepository;
import com.appraisehub.service.AppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppraisalServiceImpl implements AppraisalService {

    @Autowired
    private AppraisalRepository appraisalRepository;

    private AppraisalResponseDTO convertToResponseDTO(Appraisal appraisal) {
        AppraisalResponseDTO responseDTO = new AppraisalResponseDTO();
        responseDTO.setId(appraisal.getId());
        responseDTO.setEmployeeId(appraisal.getEmployeeId());
        responseDTO.setReviewerId(appraisal.getReviewerId());
        responseDTO.setCycleId(appraisal.getCycleId());
        responseDTO.setStatus(appraisal.getStatus());
        responseDTO.setFinalScore(appraisal.getFinalScore());
        responseDTO.setSelfComments(appraisal.getSelfComments());
        responseDTO.setManagerComments(appraisal.getManagerComments());
        responseDTO.setSubmittedAt(appraisal.getSubmittedAt());
        responseDTO.setCreatedAt(appraisal.getCreatedAt());
        responseDTO.setUpdatedAt(appraisal.getUpdatedAt());
        return responseDTO;
    }

    private Appraisal convertToEntity(AppraisalRequestDTO requestDTO) {
        Appraisal appraisal = new Appraisal();
        appraisal.setEmployeeId(requestDTO.getEmployeeId());
        appraisal.setReviewerId(requestDTO.getReviewerId());
        appraisal.setCycleId(requestDTO.getCycleId());
        appraisal.setSelfComments(requestDTO.getSelfComments());
        appraisal.setManagerComments(requestDTO.getManagerComments());
        appraisal.setFinalScore(requestDTO.getFinalScore());
        return appraisal;
    }

    @Override
    public List<AppraisalResponseDTO> getAllAppraisals() {
        return appraisalRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppraisalResponseDTO getAppraisalById(Long id) {
        Appraisal appraisal = appraisalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + id));
        return convertToResponseDTO(appraisal);
    }

    @Override
    public AppraisalResponseDTO createAppraisal(AppraisalRequestDTO requestDTO) {
        Appraisal appraisal = convertToEntity(requestDTO);
        Appraisal savedAppraisal = appraisalRepository.save(appraisal);
        return convertToResponseDTO(savedAppraisal);
    }

    @Override
    public AppraisalResponseDTO updateAppraisal(Long id, AppraisalRequestDTO requestDTO) {
        Appraisal existing = appraisalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + id));
        existing.setEmployeeId(requestDTO.getEmployeeId());
        existing.setReviewerId(requestDTO.getReviewerId());
        existing.setCycleId(requestDTO.getCycleId());
        existing.setSelfComments(requestDTO.getSelfComments());
        existing.setManagerComments(requestDTO.getManagerComments());
        existing.setFinalScore(requestDTO.getFinalScore());
        Appraisal updatedAppraisal = appraisalRepository.save(existing);
        return convertToResponseDTO(updatedAppraisal);
    }

    @Override
    public void deleteAppraisal(Long id) {
        appraisalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + id));
        appraisalRepository.deleteById(id);
    }

    @Override
    public List<AppraisalResponseDTO> getAppraisalsByEmployeeId(Long employeeId) {
        return appraisalRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppraisalResponseDTO> getAppraisalsByCycleId(Long cycleId) {
        return appraisalRepository.findByCycleId(cycleId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppraisalResponseDTO> getAppraisalsByReviewerId(Long reviewerId) {
        return appraisalRepository.findByReviewerId(reviewerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppraisalResponseDTO submitAppraisal(Long id) {
        Appraisal appraisal = appraisalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + id));
        appraisal.setStatus(AppraisalStatus.SUBMITTED);
        appraisal.setSubmittedAt(LocalDateTime.now());
        Appraisal updatedAppraisal = appraisalRepository.save(appraisal);
        return convertToResponseDTO(updatedAppraisal);
    }

    @Override
    public AppraisalResponseDTO updateAppraisalStatus(Long id, AppraisalStatus status) {
        Appraisal appraisal = appraisalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + id));
        appraisal.setStatus(status);
        Appraisal updatedAppraisal = appraisalRepository.save(appraisal);
        return convertToResponseDTO(updatedAppraisal);
    }
}
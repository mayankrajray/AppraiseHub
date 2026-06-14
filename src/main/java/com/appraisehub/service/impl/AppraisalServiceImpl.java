package com.appraisehub.service.impl;

import com.appraisehub.dto.*;
import com.appraisehub.entity.Appraisal;
import com.appraisehub.entity.Notification;
import com.appraisehub.entity.User;
import com.appraisehub.enums.AppraisalStatus;
import com.appraisehub.enums.CycleStatus;
import com.appraisehub.enums.Role;
import com.appraisehub.exception.DuplicateResourceException;
import com.appraisehub.exception.InvalidStatusTransitionException;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.exception.UnauthorizedAccessException;
import com.appraisehub.mappers.AppraisalMapper;
import com.appraisehub.repository.AppraisalRepository;
import com.appraisehub.repository.UserRepository;
import com.appraisehub.service.AppraisalService;
import com.appraisehub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppraisalServiceImpl implements AppraisalService {

    @Autowired
    private AppraisalRepository appraisalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public AppraisalResponseDTO createAppraisal(CreateAppraisalRequestDTO request) {
        validateCycleYear(request.getCycleName(),
                request.getCycleStartDate(), request.getCycleEndDate());

        LocalDate yearStart = getYearStart(request.getCycleStartDate());
        LocalDate yearEnd = getYearEnd(request.getCycleStartDate());

        if (appraisalRepository.existsByEmployeeIdAndCycleStartDateBetween(
                request.getEmployeeId(), yearStart, yearEnd)) {
            throw new DuplicateResourceException(
                    "Employee already has an appraisal cycle for year "
                            + request.getCycleStartDate().getYear());
        }

        if (appraisalRepository.existsByCycleNameAndEmployeeId(
                request.getCycleName(), request.getEmployeeId())) {
            throw new DuplicateResourceException(
                    "Appraisal already exists for this employee in cycle: "
                            + request.getCycleName());
        }

        User employee = findUserById(request.getEmployeeId());
        User manager = findUserById(request.getManagerId());

        if (manager.getRole() != Role.MANAGER) {
            throw new RuntimeException(
                    "The assigned manager must have the MANAGER role");
        }

        Appraisal appraisal = Appraisal.builder()
                .cycleName(request.getCycleName())
                .cycleStartDate(request.getCycleStartDate())
                .cycleEndDate(request.getCycleEndDate())
                .cycleStatus(CycleStatus.ACTIVE)
                .employee(employee)
                .manager(manager)
                .appraisalStatus(AppraisalStatus.PENDING)
                .build();

        appraisalRepository.save(appraisal);

        notificationService.send(
                employee.getId(),
                "Appraisal cycle started",
                "Your appraisal for cycle '" + request.getCycleName()
                        + "' has been created. Please submit your self-assessment.",
                Notification.Type.CYCLE_STARTED,
                null);

        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public BulkCycleResponseDTO createBulkCycle(BulkCycleRequestDTO request) {
        validateCycleYear(request.getCycleName(),
                request.getCycleStartDate(), request.getCycleEndDate());

        LocalDate yearStart = getYearStart(request.getCycleStartDate());
        LocalDate yearEnd = getYearEnd(request.getCycleStartDate());

        List<User> employees = request.getDepartmentId() != null
                ? userRepository.findByDepartmentIdAndIsActiveTrue(
                        request.getDepartmentId())
                .stream()
                .filter(u -> u.getRole() == Role.EMPLOYEE
                        || u.getRole() == Role.MANAGER)
                .collect(Collectors.toList())
                : userRepository.findByIsActiveTrue()
                .stream()
                .filter(u -> u.getRole() == Role.EMPLOYEE
                        || u.getRole() == Role.MANAGER)
                .collect(Collectors.toList());

        int created = 0, skippedAlreadyExists = 0, skippedNoManager = 0;

        for (User employee : employees) {
            if (employee.getManager() == null) {
                skippedNoManager++;
                continue;
            }
            if (appraisalRepository.existsByEmployeeIdAndCycleStartDateBetween(
                    employee.getId(), yearStart, yearEnd)) {
                skippedAlreadyExists++;
                continue;
            }

            Appraisal appraisal = Appraisal.builder()
                    .cycleName(request.getCycleName())
                    .cycleStartDate(request.getCycleStartDate())
                    .cycleEndDate(request.getCycleEndDate())
                    .cycleStatus(CycleStatus.ACTIVE)
                    .employee(employee)
                    .manager(employee.getManager())
                    .appraisalStatus(AppraisalStatus.PENDING)
                    .build();

            appraisalRepository.save(appraisal);

            notificationService.send(
                    employee.getId(),
                    "Appraisal cycle started",
                    "Your appraisal for cycle '" + request.getCycleName()
                            + "' has been created. Please submit your self-assessment.",
                    Notification.Type.CYCLE_STARTED,
                    null);

            created++;
        }

        return new BulkCycleResponseDTO(
                request.getCycleName(),
                employees.size(),
                created,
                skippedAlreadyExists,
                skippedNoManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppraisalResponseDTO> getAllAppraisals() {
        return appraisalRepository.findAllWithDetails()
                .stream()
                .map(AppraisalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppraisalResponseDTO> getMyAppraisals(Long employeeId) {
        return appraisalRepository.findByEmployeeId(employeeId)
                .stream()
                .map(AppraisalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppraisalResponseDTO> getTeamAppraisals(Long managerId) {
        return appraisalRepository.findByManagerId(managerId)
                .stream()
                .map(AppraisalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppraisalResponseDTO getAppraisalById(Long appraisalId, Long requesterId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        boolean isEmployee = appraisal.getEmployee().getId().equals(requesterId);
        boolean isManager = appraisal.getManager().getId().equals(requesterId);
        if (!isEmployee && !isManager) {
            throw new UnauthorizedAccessException(
                    "Access denied: you are not part of this appraisal");
        }
        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO saveSelfAssessmentDraft(Long appraisalId,
                                                        SelfAssessmentRequestDTO request, Long employeeId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        requireEmployee(appraisal, employeeId);

        AppraisalStatus status = appraisal.getAppraisalStatus();
        if (status != AppraisalStatus.PENDING
                && status != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new InvalidStatusTransitionException(
                    "Cannot save draft. Current status: " + status);
        }

        applySelfAssessmentFields(appraisal, request);
        appraisal.setAppraisalStatus(AppraisalStatus.EMPLOYEE_DRAFT);
        appraisalRepository.save(appraisal);
        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO submitSelfAssessment(Long appraisalId,
                                                     SelfAssessmentRequestDTO request, Long employeeId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        requireEmployee(appraisal, employeeId);

        AppraisalStatus status = appraisal.getAppraisalStatus();
        if (status != AppraisalStatus.PENDING
                && status != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new InvalidStatusTransitionException(
                    "Cannot submit self-assessment. Current status: " + status);
        }

        applySelfAssessmentFields(appraisal, request);
        appraisal.setAppraisalStatus(AppraisalStatus.SELF_SUBMITTED);
        appraisal.setSubmittedAt(LocalDateTime.now());
        appraisalRepository.save(appraisal);

        notificationService.send(
                appraisal.getManager().getId(),
                "Self-assessment submitted",
                appraisal.getEmployee().getFullName()
                        + " has submitted their self-assessment for '"
                        + appraisal.getCycleName() + "'. Please review and rate.",
                Notification.Type.SELF_ASSESSMENT_SUBMITTED,
                null);

        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO saveManagerReviewDraft(Long appraisalId,
                                                       ManagerReviewRequestDTO request, Long managerId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        requireManager(appraisal, managerId);

        AppraisalStatus status = appraisal.getAppraisalStatus();
        if (status != AppraisalStatus.SELF_SUBMITTED
                && status != AppraisalStatus.MANAGER_DRAFT) {
            throw new InvalidStatusTransitionException(
                    "Cannot save manager draft. Current status: " + status);
        }

        applyManagerReviewFields(appraisal, request);
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_DRAFT);
        appraisalRepository.save(appraisal);
        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO submitManagerReview(Long appraisalId,
                                                    ManagerReviewRequestDTO request, Long managerId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        requireManager(appraisal, managerId);

        AppraisalStatus status = appraisal.getAppraisalStatus();
        if (status != AppraisalStatus.SELF_SUBMITTED
                && status != AppraisalStatus.MANAGER_DRAFT) {
            throw new InvalidStatusTransitionException(
                    "Cannot submit manager review. Current status: " + status);
        }

        applyManagerReviewFields(appraisal, request);
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_REVIEWED);
        appraisalRepository.save(appraisal);

        List<User> hrUsers = userRepository.findByRoleAndIsActiveTrue(Role.HR);
        for (User hr : hrUsers) {
            notificationService.send(
                    hr.getId(),
                    "Appraisal ready for approval",
                    appraisal.getEmployee().getFullName()
                            + "'s appraisal for '" + appraisal.getCycleName()
                            + "' is ready for your approval.",
                    Notification.Type.MANAGER_REVIEW_DONE,
                    null);
        }

        notificationService.send(
                appraisal.getEmployee().getId(),
                "Your appraisal has been reviewed",
                "Your manager has completed their review for '"
                        + appraisal.getCycleName() + "'. Awaiting HR approval.",
                Notification.Type.MANAGER_REVIEW_DONE,
                null);

        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO approveAppraisal(Long appraisalId) {
        Appraisal appraisal = findAppraisalById(appraisalId);

        if (appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_REVIEWED) {
            throw new InvalidStatusTransitionException(
                    "Cannot approve. Current status: "
                            + appraisal.getAppraisalStatus());
        }

        appraisal.setAppraisalStatus(AppraisalStatus.APPROVED);
        appraisal.setApprovedAt(LocalDateTime.now());
        appraisalRepository.save(appraisal);

        notificationService.send(
                appraisal.getEmployee().getId(),
                "Appraisal approved",
                "Your appraisal for '" + appraisal.getCycleName()
                        + "' has been approved. Please review and acknowledge.",
                Notification.Type.APPRAISAL_APPROVED,
                null);

        return AppraisalMapper.toResponse(appraisal);
    }

    @Override
    @Transactional
    public AppraisalResponseDTO acknowledgeAppraisal(Long appraisalId,
                                                     Long employeeId) {
        Appraisal appraisal = findAppraisalById(appraisalId);
        requireEmployee(appraisal, employeeId);

        if (appraisal.getAppraisalStatus() != AppraisalStatus.APPROVED) {
            throw new InvalidStatusTransitionException(
                    "Cannot acknowledge. Current status: "
                            + appraisal.getAppraisalStatus());
        }

        appraisal.setAppraisalStatus(AppraisalStatus.ACKNOWLEDGED);
        appraisalRepository.save(appraisal);
        return AppraisalMapper.toResponse(appraisal);
    }

    private void validateCycleYear(String cycleName,
                                   LocalDate startDate, LocalDate endDate) {
        LocalDate yearStart = getYearStart(startDate);
        LocalDate yearEnd = getYearEnd(startDate);
        List<String> existingCycleNames = appraisalRepository
                .findDistinctCycleNamesInYear(yearStart, yearEnd);

        if (!existingCycleNames.isEmpty()
                && !existingCycleNames.contains(cycleName)) {
            throw new DuplicateResourceException(
                    "An appraisal cycle already exists for year "
                            + startDate.getYear() + ": " + existingCycleNames.get(0));
        }

        appraisalRepository.findFirstByCycleNameAndCycleStartDateBetween(
                        cycleName, yearStart, yearEnd)
                .ifPresent(existing -> {
                    if (!existing.getCycleStartDate().equals(startDate)
                            || !existing.getCycleEndDate().equals(endDate)) {
                        throw new DuplicateResourceException(
                                "Cycle dates must match existing cycle '"
                                        + cycleName + "'.");
                    }
                });
    }

    private LocalDate getYearStart(LocalDate date) {
        return LocalDate.of(date.getYear(), 1, 1);
    }

    private LocalDate getYearEnd(LocalDate date) {
        return LocalDate.of(date.getYear(), 12, 31);
    }

    private void requireEmployee(Appraisal appraisal, Long employeeId) {
        if (!appraisal.getEmployee().getId().equals(employeeId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: this is not your appraisal");
        }
    }

    private void requireManager(Appraisal appraisal, Long managerId) {
        if (!appraisal.getManager().getId().equals(managerId)
                && !appraisal.getEmployee().getId().equals(managerId)) {
            throw new UnauthorizedAccessException(
                    "Access denied: you are not the manager for this appraisal");
        }
    }

    private void applySelfAssessmentFields(Appraisal appraisal,
                                           SelfAssessmentRequestDTO request) {
        appraisal.setWhatWentWell(request.getWhatWentWell());
        appraisal.setWhatToImprove(request.getWhatToImprove());
        appraisal.setAchievements(request.getAchievements());
        appraisal.setSelfRating(request.getSelfRating());
    }

    private void applyManagerReviewFields(Appraisal appraisal,
                                          ManagerReviewRequestDTO request) {
        appraisal.setManagerStrengths(request.getManagerStrengths());
        appraisal.setManagerImprovements(request.getManagerImprovements());
        appraisal.setManagerComments(request.getManagerComments());
        appraisal.setManagerRating(request.getManagerRating());
    }

    private Appraisal findAppraisalById(Long id) {
        return appraisalRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal", id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
package com.appraisehub.entity;

import com.appraisehub.enums.AppraisalStatus;
import com.appraisehub.enums.CycleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appraisals", uniqueConstraints = @UniqueConstraint(
        name = "uq_cycle_employee",
        columnNames = {"cycle_name", "employee_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cycle_name", nullable = false, length = 150)
    private String cycleName;

    @Column(name = "cycle_start_date", nullable = false)
    private LocalDate cycleStartDate;

    @Column(name = "cycle_end_date", nullable = false)
    private LocalDate cycleEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_status", nullable = false, length = 20)
    @Builder.Default
    private CycleStatus cycleStatus = CycleStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    // Self assessment fields
    @Column(name = "what_went_well", columnDefinition = "TEXT")
    private String whatWentWell;

    @Column(name = "what_to_improve", columnDefinition = "TEXT")
    private String whatToImprove;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "self_rating")
    private Integer selfRating;

    // Manager review fields
    @Column(name = "manager_strengths", columnDefinition = "TEXT")
    private String managerStrengths;

    @Column(name = "manager_improvements", columnDefinition = "TEXT")
    private String managerImprovements;

    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;

    @Column(name = "manager_rating")
    private Integer managerRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "appraisal_status", nullable = false, length = 25)
    @Builder.Default
    private AppraisalStatus appraisalStatus = AppraisalStatus.PENDING;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
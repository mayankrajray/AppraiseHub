package com.appraisehub.repository;

import com.appraisehub.entity.Appraisal;
import com.appraisehub.enums.AppraisalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppraisalRepository extends JpaRepository<Appraisal, Long> {

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee e
            left join fetch e.department
            join fetch a.manager
            """)
    List<Appraisal> findAllWithDetails();

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee e
            left join fetch e.department
            join fetch a.manager
            where a.id = :id
            """)
    Optional<Appraisal> findByIdWithDetails(@Param("id") Long id);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee e
            left join fetch e.department
            join fetch a.manager
            where a.employee.id = :employeeId
            """)
    List<Appraisal> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee e
            left join fetch e.department
            join fetch a.manager
            where a.manager.id = :managerId
            """)
    List<Appraisal> findByManagerId(@Param("managerId") Long managerId);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee
            join fetch a.manager
            where a.cycleName = :cycleName
            """)
    List<Appraisal> findByCycleName(@Param("cycleName") String cycleName);

    boolean existsByCycleNameAndEmployeeId(String cycleName, Long employeeId);

    boolean existsByEmployeeIdAndCycleStartDateBetween(
            Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("""
            select distinct a.cycleName
            from Appraisal a
            where a.cycleStartDate between :startDate and :endDate
            """)
    List<String> findDistinctCycleNamesInYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<Appraisal> findFirstByCycleNameAndCycleStartDateBetween(
            String cycleName, LocalDate startDate, LocalDate endDate);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee
            join fetch a.manager
            where a.cycleName = :cycleName
              and a.employee.id = :employeeId
            """)
    Optional<Appraisal> findByCycleNameAndEmployeeId(
            @Param("cycleName") String cycleName,
            @Param("employeeId") Long employeeId);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee
            join fetch a.manager
            where a.cycleName = :cycleName
              and a.appraisalStatus = :status
            """)
    List<Appraisal> findByCycleNameAndAppraisalStatus(
            @Param("cycleName") String cycleName,
            @Param("status") AppraisalStatus status);

    @Query("""
            select a
            from Appraisal a
            join fetch a.employee
            join fetch a.manager
            where a.cycleName = :cycleName
              and a.manager.id = :managerId
            """)
    List<Appraisal> findByCycleNameAndManagerId(
            @Param("cycleName") String cycleName,
            @Param("managerId") Long managerId);
}
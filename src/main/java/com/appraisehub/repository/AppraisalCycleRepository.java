package com.appraisehub.repository;

import com.appraisehub.model.AppraisalCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppraisalCycleRepository extends JpaRepository<AppraisalCycle,Long> {
}

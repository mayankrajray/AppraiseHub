package com.appraisehub.repository;

import com.appraisehub.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal,Long> {

    //Manager wants to see all goals for one employee
    List<Goal> findByUserId(Long userId);

    //HR wants to see all goals in one cycle
    List<Goal> findByCycleId(Long cycleId);

    //Manager wants goals for specific employee in specific cycle
    List<Goal> findByUserIdAndCycleId(Long userId, Long cycleId);
}

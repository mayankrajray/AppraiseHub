package com.appraisehub.repository;

import com.appraisehub.entity.User;
import com.appraisehub.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByDepartmentId(Long departmentId);
    List<User> findByRole(Role role);
    List<User> findByRoleAndIsActiveTrue(Role role);
    List<User> findByIsActiveTrue();
    List<User> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    boolean existsByEmail(String email);

    @Query("""
    select u
    from User u
    left join fetch u.department
    left join fetch u.manager
    where u.email = :email
    """)
    Optional<User> findByEmailWithDetails(@Param("email") String email);
}

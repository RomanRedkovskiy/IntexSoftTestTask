package com.example.employeeservice.repository;

import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.enums.EmployeeRole;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findAllByDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"phones"})
    Optional<Employee> findByIdAndDeletedFalse(Long id);

    boolean existsByRoleAndDepartmentIdAndDeletedFalse(EmployeeRole role, Long departmentId);

    @EntityGraph(attributePaths = {"phones"})
    List<Employee> findByDepartmentIdAndDeletedFalse(Long departmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.manager = NULL WHERE e.manager.id = :managerId")
    void updateManagerToNullForSubordinates(Long managerId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.departmentId = NULL WHERE e.departmentId = :departmentId")
    void updateDepartmentToNullByDepartmentId(Long departmentId);

}

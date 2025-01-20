package com.example.employeeservice.repository;

import com.example.employeeservice.model.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e " +
            "WHERE e.deleted = false")
    Page<Employee> findAllByNotDeleted(Pageable pageable);

    @EntityGraph(attributePaths = {"phones"})
    @Query("SELECT e FROM Employee e " +
            "WHERE e.id = :id AND e.deleted = false")
    Optional<Employee> findByIdAndNotDeleted(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.manager = NULL WHERE e.manager.id = :managerId")
    void updateManagerToNullForSubordinates(Long managerId);

}

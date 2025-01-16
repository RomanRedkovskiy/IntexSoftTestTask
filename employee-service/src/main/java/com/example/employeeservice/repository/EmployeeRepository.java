package com.example.employeeservice.repository;

import com.example.employeeservice.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT d FROM Employee d " +
            "WHERE d.deleted = false")
    Page<Employee> findAllByNotDeleted(Pageable pageable);

    @Query("SELECT d FROM Employee d " +
            "WHERE d.id = :id AND d.deleted = false")
    Optional<Employee> findByIdAndNotDeleted(Long id);

}

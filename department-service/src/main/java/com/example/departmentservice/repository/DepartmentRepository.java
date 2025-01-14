package com.example.departmentservice.repository;

import com.example.departmentservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d " +
            "WHERE d.deleted = false")
    List<Department> findAllByNotDeleted();

    @Query("SELECT d FROM Department d " +
            "WHERE d.id = :id AND d.deleted = false")
    Optional<Department> findByIdAndNotDeleted(Long id);

}

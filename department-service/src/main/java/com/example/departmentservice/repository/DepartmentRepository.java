package com.example.departmentservice.repository;

import com.example.departmentservice.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByDeletedFalse();

    Optional<Department> findByIdAndDeletedFalse(Long id);

}

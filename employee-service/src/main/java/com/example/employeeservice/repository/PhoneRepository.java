package com.example.employeeservice.repository;

import com.example.employeeservice.model.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query("SELECT p FROM Phone p JOIN FETCH p.employee WHERE p.id = :id")
    Optional<Phone> findByIdWithEmployee(Long id);

    @Query("SELECT p FROM Phone p " +
            "WHERE p.employee.id = :employeeId")
    List<Phone> findByEmployeeId(Long employeeId);

    @Query("SELECT p.phone FROM Phone p " +
            "WHERE p.phone IN :phoneList")
    List<String> findByPhoneList(List<String> phoneList);
}

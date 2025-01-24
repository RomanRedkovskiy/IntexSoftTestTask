package com.example.employeeservice.repository;

import com.example.employeeservice.model.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByIdAndDeletedFalse(Long id);

    List<Phone> findByEmployeeIdAndDeletedFalseAndEmployeeDeletedFalse(Long employeeId);

    @Query("SELECT p.phone FROM Phone p " +
            "WHERE p.phone IN :phoneList")
    List<String> findByPhoneIn(List<String> phoneList);

    boolean existsByPhoneAndIdNot(String phone, Long id);

}

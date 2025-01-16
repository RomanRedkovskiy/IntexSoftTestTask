package com.example.employeeservice.service;

import com.example.employeeservice.dto.employee.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoOut;
import com.example.employeeservice.dto.phone.PhoneFullDtoIn;
import com.example.employeeservice.dto.phone.PhoneFullDtoOut;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    Page<EmployeeDtoOut> getEmployeesDto(int page, int size);

    EmployeeDtoOut getEmployeeDto(Long id);

    PhoneFullDtoOut getPhoneDtoById(Long id);

    List<PhoneDtoOut> getPhonesByEmployeeId(Long employeeId);

    EmployeeDtoOut createEmployee(EmployeeCreateDtoIn employeeCreateDtoIn);

    PhoneFullDtoOut createPhone(PhoneFullDtoIn phoneDtoIn);

    EmployeeDtoOut updateEmployee(EmployeeUpdateDtoIn employeeUpdateDtoIn);

    PhoneFullDtoOut updatePhone(PhoneFullDtoIn phoneDtoIn);

    void deleteEmployee(Long id);

    void deletePhone(Long id);
}

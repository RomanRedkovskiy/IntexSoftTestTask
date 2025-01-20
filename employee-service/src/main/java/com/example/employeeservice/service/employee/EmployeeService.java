package com.example.employeeservice.service.employee;

import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeDtoOut> getEmployeesDto(Pageable pageable);

    EmployeeDtoOut getEmployeeDto(Long id);

    EmployeeDtoOut createEmployee(EmployeeCreateDtoIn employeeCreateDtoIn);

    EmployeeDtoOut updateEmployee(EmployeeUpdateDtoIn employeeUpdateDtoIn);

    void deleteEmployee(Long id);

}

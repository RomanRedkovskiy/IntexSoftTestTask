package com.example.employeeservice.service.employee;

import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.out.EmployeeFullDtoOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Page<EmployeeDtoOut> getDtoPage(Pageable pageable);

    EmployeeFullDtoOut getDto(Long id);

    List<EmployeeDtoOut> getDtoListByDepartmentId(Long departmentId);

    EmployeeDtoOut create(EmployeeCreateDtoIn employeeCreateDtoIn);

    EmployeeDtoOut update(EmployeeUpdateDtoIn employeeUpdateDtoIn);

    void delete(Long id);

    void deleteDepartmentReference(Long departmentId);

}

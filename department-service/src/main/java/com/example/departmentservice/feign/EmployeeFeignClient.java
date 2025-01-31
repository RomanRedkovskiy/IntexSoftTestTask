package com.example.departmentservice.feign;

import com.example.departmentservice.dto.employee.EmployeeDtoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "EMPLOYEE-SERVICE", url = "${employee.service.url}")
public interface EmployeeFeignClient {

    @GetMapping("api/v1/employees/department/{id}")
    List<EmployeeDtoOut> getEmployeesByDepartmentId(@PathVariable Long id);

    @DeleteMapping("api/v1/employees/department/{departmentId}")
    void deleteDepartmentReference(@PathVariable Long departmentId);

}

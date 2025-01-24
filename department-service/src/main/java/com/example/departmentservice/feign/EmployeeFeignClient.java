package com.example.departmentservice.feign;

import com.example.departmentservice.dto.employee.EmployeeDtoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "department-service", url = "${employee.service.url}")
public interface EmployeeFeignClient {

    @GetMapping("employees/department/{id}")
    List<EmployeeDtoOut> getEmployeesByDepartmentId(@PathVariable Long id);

    @DeleteMapping("employees/department/{departmentId}")
    void deleteDepartmentReference(@PathVariable Long departmentId);

}

package com.example.employeeservice.feign;

import com.example.employeeservice.dto.department.DepartmentDtoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DEPARTMENT-SERVICE", url = "${department.service.url}")
public interface DepartmentFeignClient {

    @GetMapping("api/v1/departments/{id}")
    DepartmentDtoOut getDepartmentById(@PathVariable Long id);

}

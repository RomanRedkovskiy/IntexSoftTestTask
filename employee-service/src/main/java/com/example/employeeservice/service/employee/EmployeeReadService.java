package com.example.employeeservice.service.employee;

import com.example.employeeservice.model.entity.Employee;

public interface EmployeeReadService {

    Employee getById(Long id);
}

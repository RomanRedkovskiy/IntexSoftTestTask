package com.example.employeeservice.service.employee.retriever;

import com.example.employeeservice.model.entity.Employee;

public interface EmployeeRetrieverService {

    Employee getEmployeeById(Long id);
}

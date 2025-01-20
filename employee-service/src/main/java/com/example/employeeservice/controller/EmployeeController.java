package com.example.employeeservice.controller;

import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Validated
@Tag(name = "Employee Controller", description = "Controller for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("page")
    @Operation(summary = "Retrieve employees page", description = "Fetch a page of available employees with filtering")
    public Page<EmployeeDtoOut> getAllEmployees(Pageable pageable) {
        return employeeService.getEmployeesDto(pageable);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retrieve an employee by ID", description = "Fetch details of an employee using its unique identifier.")
    public EmployeeDtoOut getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeDto(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new employee", description = "Add a new employee to the system.")
    public EmployeeDtoOut createEmployee(@RequestBody @Valid EmployeeCreateDtoIn employeeCreateDtoIn) {
        return employeeService.createEmployee(employeeCreateDtoIn);
    }

    @PutMapping
    @Operation(summary = "Update an existing employee", description = "Edit details of an employee. This method won't affect employee's phones")
    public EmployeeDtoOut updateEmployee(@RequestBody @Valid EmployeeUpdateDtoIn employeeUpdateDtoIn) {
        return employeeService.updateEmployee(employeeUpdateDtoIn);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an employee", description = "Remove an employee from the system using its ID.")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}


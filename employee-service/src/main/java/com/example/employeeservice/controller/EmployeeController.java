package com.example.employeeservice.controller;

import com.example.employeeservice.dto.employee.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoOut;
import com.example.employeeservice.dto.phone.PhoneFullDtoIn;
import com.example.employeeservice.dto.phone.PhoneFullDtoOut;
import com.example.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Controller", description = "Controller for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("page")
    @Operation(summary = "Retrieve employees page", description = "Fetch a page of available employees with filtering")
    public Page<EmployeeDtoOut> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return employeeService.getEmployeesDto(page, size);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retrieve an employee by ID", description = "Fetch details of an employee using its unique identifier.")
    public EmployeeDtoOut getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeDto(id);
    }

    @GetMapping("phone/{id}")
    @Operation(summary = "Retrieve a phone by ID", description = "Fetch details of a phone using its unique identifier.")
    public PhoneFullDtoOut getPhoneById(@PathVariable Long id) {
        return employeeService.getPhoneDtoById(id);
    }

    @GetMapping("{employeeId}/phone")
    @Operation(summary = "Retrieve a phone list by employee ID", description = "Fetch a list of phones using employee unique identifier.")
    public List<PhoneDtoOut> getPhonesByEmployeeId(@PathVariable Long employeeId) {
        return employeeService.getPhonesByEmployeeId(employeeId);
    }

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Add a new employee to the system.")
    public EmployeeDtoOut createEmployee(@RequestBody @Valid EmployeeCreateDtoIn employeeCreateDtoIn) {
        return employeeService.createEmployee(employeeCreateDtoIn);
    }

    @PostMapping("phone")
    @Operation(summary = "Add a new phone", description = "Add a new employee's phone to the system.")
    public PhoneFullDtoOut addPhone(@RequestBody PhoneFullDtoIn phoneDtoIn) {
        return employeeService.createPhone(phoneDtoIn);
    }

    @PutMapping
    @Operation(summary = "Update an existing employee", description = "Edit details of an employee.")
    public EmployeeDtoOut updateEmployee(@RequestBody @Valid EmployeeUpdateDtoIn employeeUpdateDtoIn) {
        return employeeService.updateEmployee(employeeUpdateDtoIn);
    }

    @PutMapping("phone")
    @Operation(summary = "Update an existing phone", description = "Edit details an existing phone.")
    public PhoneFullDtoOut updatePhone(@RequestBody @Valid PhoneFullDtoIn phoneDtoIn) {
        return employeeService.updatePhone(phoneDtoIn);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an employee", description = "Remove an employee from the system using its ID.")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @DeleteMapping("phone/{id}")
    @Operation(summary = "Delete a phone", description = "Remove an employee from the system using its ID.")
    public void deletePhone(@PathVariable Long id) {
        employeeService.deletePhone(id);
    }

}


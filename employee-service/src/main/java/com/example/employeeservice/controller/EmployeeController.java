package com.example.employeeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Controller", description = "Controller for managing departments")
public class EmployeeController {

    @GetMapping("page")
    @Operation(summary = "Retrieve employees page", description = "Fetch a page of available employees with filtering")
    public List<String> getAllEmployees() {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve an employee by ID", description = "Fetch details of an employee using its unique identifier.")
    public String getEmployeeById(@PathVariable Long id) {
        return null;
    }

    @PostMapping("")
    @Operation(summary = "Create a new employee", description = "Add a new employee to the system.")
    public String createEmployee(@RequestBody String employee) {
        return null;
    }

    @PostMapping("phone")
    @Operation(summary = "Add a new phone", description = "Add a new employee's phone to the system.")
    public String addPhone(@RequestBody String employee) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing employee", description = "Edit details of an existing employee identified by its ID.")
    public String updateEmployee(@PathVariable Long id, @RequestBody String employee) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a employee", description = "Remove an employee from the system using its ID.")
    public String deleteEmployee(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/phone/{phone}")
    @Operation(summary = "Delete a employee", description = "Remove an employee from the system using its ID.")
    public String deletePhone(@PathVariable String phone) {
        return null;
    }

}


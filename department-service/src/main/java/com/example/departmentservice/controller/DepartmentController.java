package com.example.departmentservice.controller;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;
import com.example.departmentservice.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "Controller for managing departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "Retrieve all departments", description = "Fetch a list of all available departments.")
    public List<DepartmentDtoOut> getAllDepartments() {
        return departmentService.getDepartmentsDto();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a department by ID", description = "Fetch details of a department using its unique identifier.")
    public DepartmentDtoOut getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentDto(id);
    }

    @PostMapping
    @Operation(summary = "Create a new department", description = "Add a new department to the system.")
    public DepartmentDtoOut createDepartment(@RequestBody @Valid DepartmentDtoIn departmentDtoIn) {
        return departmentService.createDepartment(departmentDtoIn);
    }

    @PutMapping
    @Operation(summary = "Update an existing department", description = "Edit details of an existing department identified by its ID.")
    public DepartmentDtoOut updateDepartment(@RequestBody DepartmentDtoIn departmentDtoIn) {
        return departmentService.updateDepartment(departmentDtoIn);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department", description = "Soft delete a department using its ID.")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}


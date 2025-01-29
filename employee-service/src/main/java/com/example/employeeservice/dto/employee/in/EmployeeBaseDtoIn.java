package com.example.employeeservice.dto.employee.in;

import com.example.employeeservice.model.enums.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class EmployeeBaseDtoIn {

    @NotNull(message = "Employee salary not found")
    private Long salary;

    @NotNull(message = "Employee role not found")
    private EmployeeRole role;

    private Long managerId;

    private Long departmentId;

}
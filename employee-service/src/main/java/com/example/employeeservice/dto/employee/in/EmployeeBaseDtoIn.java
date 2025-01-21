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

    @NotBlank(message = "Employee name not found")
    @Size(max = 255, message = "Employee name max size is 255")
    private String name;

    @NotBlank(message = "Employee surname not found")
    @Size(max = 255, message = "Employee surname max size is 255")
    private String surname;

    @NotNull(message = "Employee salary not found")
    private Long salary;

    @NotNull(message = "Employee role not found")
    private EmployeeRole role;

    private Long managerId;

    private Long departmentId;

}
package com.example.employeeservice.dto.employee.in;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmployeeUpdateDtoIn extends EmployeeBaseDtoIn {

    @NotNull(message = "Employee ID not found")
    private Long id;

}
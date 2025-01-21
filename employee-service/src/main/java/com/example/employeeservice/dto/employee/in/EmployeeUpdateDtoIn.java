package com.example.employeeservice.dto.employee.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDtoIn extends EmployeeBaseDtoIn {

    @NotNull(message = "Employee ID not found")
    private Long id;

}
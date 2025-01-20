package com.example.employeeservice.dto.employee.in;

import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class EmployeeCreateDtoIn extends EmployeeBaseDtoIn {

    @NotNull(message = "Employee phones not found")
    private Set<PhoneDtoIn> phones;
}
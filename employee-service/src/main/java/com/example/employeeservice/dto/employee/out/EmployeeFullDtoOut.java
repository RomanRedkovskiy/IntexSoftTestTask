package com.example.employeeservice.dto.employee.out;

import com.example.employeeservice.dto.department.DepartmentDtoOut;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class EmployeeFullDtoOut extends EmployeeBaseDtoOut {

    @JsonProperty(value = "department")
    private DepartmentDtoOut department;

}

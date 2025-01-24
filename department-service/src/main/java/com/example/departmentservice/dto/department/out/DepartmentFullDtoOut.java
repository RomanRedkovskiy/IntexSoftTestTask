package com.example.departmentservice.dto.department.out;

import com.example.departmentservice.dto.employee.EmployeeDtoOut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentFullDtoOut extends DepartmentDtoOut {

    List<EmployeeDtoOut> employees;
}

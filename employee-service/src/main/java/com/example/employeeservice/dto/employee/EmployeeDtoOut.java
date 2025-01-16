package com.example.employeeservice.dto.employee;

import com.example.employeeservice.dto.phone.PhoneDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoOut;
import com.example.employeeservice.model.enums.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDtoOut {

    private Long id;

    private String name;

    private String surname;

    private Long salary;

    private EmployeeRole role;

    private Long managerId;

    private Long departmentId;

    private Set<PhoneDtoOut> phones;

}

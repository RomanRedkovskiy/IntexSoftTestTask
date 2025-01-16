package com.example.employeeservice.dto.employee;

import com.example.employeeservice.model.enums.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDtoIn {

    private Long id;

    @NotNull(message = "Employee name not found")
    @Size(max = 255, message = "Employee name max size is 255")
    private String name;

    @NotNull(message = "Employee surname not found")
    @Size(max = 255, message = "Employee surname max size is 255")
    private String surname;

    @NotNull(message = "Employee salary not found")
    @Size(max = 255, message = "Employee salary max size is 255")
    private Long salary;

    @NotNull(message = "Employee role not found")
    private EmployeeRole role;

    private Long managerId;

    private Long departmentId;
}

package com.example.employeeservice.dto.phone;

import com.example.employeeservice.dto.employee.EmployeeDtoOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneFullDtoOut {

    private Long id;

    private String phone;

    private EmployeeDtoOut employee;

}

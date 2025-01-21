package com.example.employeeservice.dto.employee.in;

import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDtoIn extends EmployeeBaseDtoIn {

    private Set<PhoneDtoIn> phones;
}
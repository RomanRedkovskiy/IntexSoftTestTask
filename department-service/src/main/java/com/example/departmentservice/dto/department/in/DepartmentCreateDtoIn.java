package com.example.departmentservice.dto.department.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class DepartmentCreateDtoIn extends DepartmentBaseDtoIn {

}

package com.example.departmentservice.dto.department.in;

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
public class DepartmentUpdateDtoIn extends DepartmentBaseDtoIn {

    @NotNull(message = "Department id not found")
    private Long id;
}

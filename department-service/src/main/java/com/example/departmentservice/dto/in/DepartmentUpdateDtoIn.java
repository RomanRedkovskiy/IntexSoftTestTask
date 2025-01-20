package com.example.departmentservice.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateDtoIn extends DepartmentBaseDtoIn {

    @NotNull(message = "Department id not found")
    private Long id;
}

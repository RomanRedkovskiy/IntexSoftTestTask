package com.example.departmentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDtoIn {

    private Long id;

    //@NotNull(message = "Department name not found")
    private String name;

    //@NotNull(message = "Department location not found")
    private String location;
}

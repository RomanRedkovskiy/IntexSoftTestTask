package com.example.departmentservice.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class DepartmentBaseDtoIn {

    @NotBlank(message = "Department name not found")
    @Size(max = 255, message = "Department name max size is 255")
    private String name;

    @NotBlank(message = "Department location not found")
    @Size(max = 255, message = "Department location max size is 255")
    private String location;

}

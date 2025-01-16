package com.example.employeeservice.dto.phone;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDtoIn {

    @NotNull(message = "Employee phone not found")
    @Size(max = 30, message = "Employee phone max size is 30")
    private String phone;

}

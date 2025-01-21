package com.example.employeeservice.dto.phone.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDtoIn {

    @NotBlank(message = "Employee phone not found")
    @Size(max = 30, message = "Employee phone max size is 30")
    private String phone;

}

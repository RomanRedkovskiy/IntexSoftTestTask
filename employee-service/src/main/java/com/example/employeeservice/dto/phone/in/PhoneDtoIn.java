package com.example.employeeservice.dto.phone.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PhoneDtoIn {

    @NotBlank(message = "Employee phone not found")
    @Size(max = 30, message = "Employee phone max size is 30")
    private String phone;

}

package com.example.employeeservice.dto.phone.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneUpdateDtoIn extends PhoneDtoIn {

    @NotNull(message = "Employee id not found")
    private Long id;

    @NotNull(message = "Phone owner not found")
    private Long employeeId;

}

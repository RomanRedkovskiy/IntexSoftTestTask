package com.example.employeeservice.dto.phone.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCreateDtoIn extends PhoneDtoIn {

    @NotNull(message = "Phone owner not found")
    private Long employeeId;

}

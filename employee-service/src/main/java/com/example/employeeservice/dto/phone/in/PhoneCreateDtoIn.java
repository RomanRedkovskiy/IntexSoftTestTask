package com.example.employeeservice.dto.phone.in;

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
public class PhoneCreateDtoIn extends PhoneDtoIn {

    @NotNull(message = "Phone owner not found")
    private Long employeeId;

}

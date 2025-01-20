package com.example.employeeservice.dto.phone.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDtoOut {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "employeeId")
    private Long employeeId;
}

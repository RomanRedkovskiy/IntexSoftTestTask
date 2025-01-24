package com.example.employeeservice.dto.phone.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDtoOut {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "employeeId")
    private Long employeeId;
}

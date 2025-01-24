package com.example.departmentservice.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

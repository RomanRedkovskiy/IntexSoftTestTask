package com.example.notificationservice.dto.phone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDtoOut implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "employeeId")
    private Long employeeId;
}

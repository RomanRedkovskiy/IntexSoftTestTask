package com.example.notificationservice.dto.employee;

import com.example.notificationservice.dto.phone.PhoneDtoOut;
import com.example.notificationservice.enums.EmployeeRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDtoOut implements Serializable {

    @JsonProperty(value = "departmentId")
    private Long departmentId;

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "surname")
    private String surname;

    @JsonProperty(value = "salary")
    private Long salary;

    @JsonProperty(value = "role")
    private EmployeeRole role;

    @JsonProperty(value = "managerId")
    private Long managerId;

    @JsonProperty(value = "phones")
    private Set<PhoneDtoOut> phones;

}

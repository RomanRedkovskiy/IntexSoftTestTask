package com.example.departmentservice.dto.employee;

import com.example.departmentservice.dto.phone.PhoneDtoOut;
import com.example.departmentservice.model.enums.EmployeeRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Data
@NoArgsConstructor
@SuperBuilder
public abstract class EmployeeBaseDtoOut {

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

package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.employee.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.EmployeeUpdateDtoIn;
import com.example.employeeservice.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "phones", ignore = true)
    Employee fromDto(EmployeeCreateDtoIn dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    void updateFromDto(EmployeeUpdateDtoIn dtoIn, @MappingTarget Employee employee);

    @Mapping(target = "phones", ignore = true)

    EmployeeDtoOut toDto(Employee employee);
}

package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(uses = {PhoneMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    Employee fromDto(EmployeeCreateDtoIn dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    void updateFromDto(EmployeeUpdateDtoIn dtoIn, @MappingTarget Employee employee);

    @Mapping(target = "managerId", source = "employee.manager.id")
    EmployeeDtoOut toDto(Employee employee);

}

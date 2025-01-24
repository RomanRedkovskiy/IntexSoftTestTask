package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.department.DepartmentDtoOut;
import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.out.EmployeeFullDtoOut;
import com.example.employeeservice.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(uses = {PhoneMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "registrationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee from(EmployeeCreateDtoIn dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "registrationTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void enrichWith(EmployeeUpdateDtoIn dtoIn, @MappingTarget Employee employee);

    @Mapping(target = "managerId", source = "employee.manager.id")
    EmployeeDtoOut to(Employee employee);

    @Mapping(target = "managerId", source = "employee.manager.id")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "id", source = "employee.id")
    @Mapping(target = "name", source = "employee.name")
    EmployeeFullDtoOut to(Employee employee, DepartmentDtoOut department);

}

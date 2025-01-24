package com.example.departmentservice.mapper;

import com.example.departmentservice.dto.department.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.department.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.department.out.DepartmentDtoOut;
import com.example.departmentservice.dto.department.out.DepartmentFullDtoOut;
import com.example.departmentservice.dto.employee.EmployeeDtoOut;
import com.example.departmentservice.model.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Department from(DepartmentCreateDtoIn dto);

    DepartmentDtoOut to(Department department);

    @Mapping(target = "employees", source = "employees")
    DepartmentFullDtoOut to(Department department, List<EmployeeDtoOut> employees);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void enrichWith(DepartmentUpdateDtoIn dtoIn, @MappingTarget Department department);
}

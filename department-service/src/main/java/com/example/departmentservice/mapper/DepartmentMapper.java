package com.example.departmentservice.mapper;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;
import com.example.departmentservice.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department fromDto(DepartmentDtoIn dto);

    DepartmentDtoOut toDto(Department department);

    void updateFromDto(DepartmentDtoIn dtoIn, @MappingTarget Department department);
}

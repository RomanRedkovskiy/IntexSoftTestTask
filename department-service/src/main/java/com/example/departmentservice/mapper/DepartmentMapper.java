package com.example.departmentservice.mapper;

import com.example.departmentservice.dto.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.out.DepartmentDtoOut;
import com.example.departmentservice.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {

    Department fromDto(DepartmentCreateDtoIn dto);

    DepartmentDtoOut toDto(Department department);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(DepartmentUpdateDtoIn dtoIn, @MappingTarget Department department);
}

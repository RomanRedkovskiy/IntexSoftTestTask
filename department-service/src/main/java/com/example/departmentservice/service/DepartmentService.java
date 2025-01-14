package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDtoOut> getDepartments();

    DepartmentDtoOut getDepartmentDto(Long id);

    DepartmentDtoOut createDepartment(DepartmentDtoIn departmentDtoIn);

    DepartmentDtoOut updateDepartment(DepartmentDtoIn departmentDtoIn);

    void deleteDepartment(Long id);

}

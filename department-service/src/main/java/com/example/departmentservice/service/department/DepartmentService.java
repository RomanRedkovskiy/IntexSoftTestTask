package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDtoOut> getDepartmentsDto();

    DepartmentDtoOut getDepartmentDto(Long id);

    DepartmentDtoOut createDepartment(DepartmentDtoIn departmentDtoIn);

    DepartmentDtoOut updateDepartment(DepartmentDtoIn departmentDtoIn);

    void deleteDepartment(Long id);

}

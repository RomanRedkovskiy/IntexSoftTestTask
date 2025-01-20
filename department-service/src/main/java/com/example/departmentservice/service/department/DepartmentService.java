package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.out.DepartmentDtoOut;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDtoOut> getDepartmentsDto();

    DepartmentDtoOut getDepartmentDto(Long id);

    DepartmentDtoOut createDepartment(DepartmentCreateDtoIn departmentCreateDtoIn);

    DepartmentDtoOut updateDepartment(DepartmentUpdateDtoIn departmentCreateDtoIn);

    void deleteDepartment(Long id);

}

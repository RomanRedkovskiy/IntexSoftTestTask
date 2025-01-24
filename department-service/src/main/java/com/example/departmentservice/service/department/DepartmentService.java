package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.department.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.department.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.department.out.DepartmentDtoOut;
import com.example.departmentservice.dto.department.out.DepartmentFullDtoOut;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDtoOut> getDtoList();

    DepartmentFullDtoOut getDto(Long id);

    DepartmentDtoOut create(DepartmentCreateDtoIn departmentCreateDtoIn);

    DepartmentDtoOut update(DepartmentUpdateDtoIn departmentCreateDtoIn);

    void delete(Long id);

}

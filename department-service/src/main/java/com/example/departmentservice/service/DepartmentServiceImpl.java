package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;
import com.example.departmentservice.dto.DepartmentFullDtoOut;
import com.example.departmentservice.mapper.DepartmentMapper;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDtoOut> getDepartments() {
        return departmentRepository.findAllByNotDeleted().stream().map(departmentMapper::toDto).toList();
    }

    @Override
    public DepartmentDtoOut getDepartmentDto(Long id) {
        return departmentMapper.toDto(getDepartment(id));
    }

    @Override
    public DepartmentDtoOut createDepartment(DepartmentDtoIn departmentDtoIn) {
        Department department = departmentMapper.fromDto(departmentDtoIn);
        // TODO: checks
        departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDtoOut updateDepartment(DepartmentDtoIn departmentDtoIn) {
        Department department = getDepartment(departmentDtoIn.getId());
        // TODO: checks
        departmentMapper.updateFromDto(departmentDtoIn, department);
        departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartment(id);
        department.setDeleted(true);
        departmentRepository.save(department);
    }

    private Department getDepartment(Long id) {
        return departmentRepository.findByIdAndNotDeleted(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with given id not found"));
    }

    private DepartmentFullDtoOut toFullDtoOut(Department department) {
        return (DepartmentFullDtoOut) departmentMapper.toDto(department);
    }
}

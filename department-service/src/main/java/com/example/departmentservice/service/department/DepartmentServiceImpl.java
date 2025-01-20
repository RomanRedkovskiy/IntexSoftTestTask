package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.DepartmentDtoIn;
import com.example.departmentservice.dto.DepartmentDtoOut;
import com.example.departmentservice.dto.DepartmentFullDtoOut;
import com.example.departmentservice.mapper.DepartmentMapper;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import com.example.departmentservice.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final MessageService messageService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDtoOut> getDepartmentsDto() {
        return departmentRepository.findAllByNotDeleted().stream().map(departmentMapper::toDto).toList();
    }

    @Override
    public DepartmentDtoOut getDepartmentDto(Long id) {
        return departmentMapper.toDto(getDepartmentById(id));
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
        Department department = getDepartmentById(departmentDtoIn.getId());
        // TODO: checks
        departmentMapper.updateFromDto(departmentDtoIn, department);
        departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        department.setDeleted(true);
        departmentRepository.save(department);
    }

    private Department getDepartmentById(Long id) {
        return departmentRepository.findByIdAndNotDeleted(id).orElseThrow(() ->
                new EntityNotFoundException(messageService.getMessage("exception.department-not-found", id)));
    }

    private DepartmentFullDtoOut toFullDtoOut(Department department) {
        return (DepartmentFullDtoOut) departmentMapper.toDto(department);
    }
}

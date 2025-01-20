package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.out.DepartmentDtoOut;
import com.example.departmentservice.dto.out.DepartmentFullDtoOut;
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
    public DepartmentDtoOut createDepartment(DepartmentCreateDtoIn departmentCreateDtoIn) {
        Department department = departmentMapper.fromDto(departmentCreateDtoIn);
        // TODO: checks
        departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDtoOut updateDepartment(DepartmentUpdateDtoIn departmentCreateDtoIn) {
        Department department = getDepartmentById(departmentCreateDtoIn.getId());
        // TODO: checks
        departmentMapper.updateFromDto(departmentCreateDtoIn, department);
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

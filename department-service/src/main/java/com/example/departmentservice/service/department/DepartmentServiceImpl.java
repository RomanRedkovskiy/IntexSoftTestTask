package com.example.departmentservice.service.department;

import com.example.departmentservice.dto.department.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.department.in.DepartmentUpdateDtoIn;
import com.example.departmentservice.dto.department.out.DepartmentDtoOut;
import com.example.departmentservice.dto.department.out.DepartmentFullDtoOut;
import com.example.departmentservice.dto.employee.EmployeeDtoOut;
import com.example.departmentservice.feign.EmployeeFeignClient;
import com.example.departmentservice.kafka.NotificationProducer;
import com.example.departmentservice.mapper.DepartmentMapper;
import com.example.departmentservice.model.entity.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import com.example.departmentservice.service.message.MessageService;
import feign.FeignException;
import feign.RetryableException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.departmentservice.exception.ExceptionManager.getResponseMessageFromFeignException;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final EmployeeFeignClient employeeFeignClient;

    private final NotificationProducer notificationProducer;

    private final MessageService messageService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<DepartmentDtoOut> getDtoList() {
        return departmentRepository.findAllByDeletedFalse().stream().map(departmentMapper::to).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public DepartmentFullDtoOut getDto(Long id) {
        Department department = getDepartmentById(id);
        List<EmployeeDtoOut> employees = getDepartmentEmployeesOrNull(id);
        return departmentMapper.to(department, employees);
    }

    @Transactional
    @Override
    public DepartmentDtoOut create(DepartmentCreateDtoIn departmentCreateDtoIn) {
        Department department = departmentMapper.from(departmentCreateDtoIn);
        departmentRepository.save(department);
        DepartmentDtoOut dto = departmentMapper.to(department);
        notificationProducer.sendCreate(dto);
        return dto;
    }

    @Transactional
    @Override
    public DepartmentDtoOut update(DepartmentUpdateDtoIn departmentCreateDtoIn) {
        Department department = getDepartmentById(departmentCreateDtoIn.getId());
        departmentMapper.enrichWith(departmentCreateDtoIn, department);
        departmentRepository.save(department);
        DepartmentDtoOut dto = departmentMapper.to(department);
        notificationProducer.sendUpdate(dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Department department = getDepartmentById(id);
        deleteDepartmentReference(id);
        department.setDeleted(true);
        departmentRepository.save(department);
        notificationProducer.sendDelete(id);
    }

    private List<EmployeeDtoOut> getDepartmentEmployeesOrNull(Long departmentId) {
        List<EmployeeDtoOut> employees = null;
        try {
            employees = employeeFeignClient.getEmployeesByDepartmentId(departmentId);
        } catch (FeignException e) {
            String message = getResponseMessageFromFeignException(e);
            log.error(message, e.getMessage(), e);
        } catch (Exception e) {
            log.error(messageService.getMessage("exception.resource-failed-on-fetching-department",
                    departmentId.toString()), e.getMessage(), e);
        }
        return employees;
    }

    private void deleteDepartmentReference(Long departmentId) {
        try {
            employeeFeignClient.deleteDepartmentReference(departmentId);
        } catch (RetryableException e) {
            log.error("Retryable exception occurred while fetching department with ID {}: {}", departmentId, e.getMessage());
            throw new DataAccessResourceFailureException(messageService.getMessage(
                    "exception.resource-failed-on-fetching-department", departmentId.toString()));
        } catch (FeignException e) {
            String message = getResponseMessageFromFeignException(e);
            throw new EntityNotFoundException(message);
        } catch (Exception e) {
            throw new DataAccessResourceFailureException(messageService.getMessage(
                    "exception.resource-failed-on-fetching-department", departmentId.toString()));
        }
    }

    private Department getDepartmentById(Long id) {
        return departmentRepository.findByIdAndDeletedFalse(id).orElseThrow(() ->
                new EntityNotFoundException(messageService.getMessage("exception.department-not-found", id)));
    }

}

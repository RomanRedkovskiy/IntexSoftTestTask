package com.example.employeeservice.service.employee;

import com.example.employeeservice.dto.department.DepartmentDtoOut;
import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.out.EmployeeFullDtoOut;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.feign.DepartmentFeignClient;
import com.example.employeeservice.kafka.NotificationProducer;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.enums.EmployeeRole;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.message.MessageService;
import com.example.employeeservice.service.phone.PhoneService;
import feign.FeignException;
import feign.RetryableException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.example.employeeservice.exception.ExceptionManager.getResponseMessageFromFeignException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeReadService employeeReadService;

    private final PhoneService phoneService;

    private final MessageService messageService;

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final DepartmentFeignClient departmentFeignClient;

    private final NotificationProducer notificationProducer;

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDtoOut> getDtoPage(Pageable pageable) {
        return employeeRepository.findAllByDeletedFalse(pageable).map(employeeMapper::to);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeFullDtoOut getDto(Long id) {
        Employee employee = employeeReadService.getById(id);
        DepartmentDtoOut departmentDtoOut = getDepartmentOrNull(employee.getDepartmentId());
        return employeeMapper.to(employee, departmentDtoOut);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDtoOut> getDtoListByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentIdAndDeletedFalse(departmentId).stream()
                .map(employeeMapper::to)
                .toList();
    }

    @Transactional
    @Override
    public EmployeeDtoOut create(EmployeeCreateDtoIn createDtoIn) {
        Employee employee = employeeMapper.from(createDtoIn);
        handleEmployeePhones(employee, createDtoIn.getPhones());
        handleEmployeeRelations(employee, createDtoIn.getManagerId(), createDtoIn.getDepartmentId());
        EmployeeDtoOut employeeDtoOut = employeeMapper.to(employeeRepository.save(employee));
        notificationProducer.sendEmployeeCreate(employeeDtoOut);
        return employeeDtoOut;
    }

    @Transactional
    @Override
    public EmployeeDtoOut update(EmployeeUpdateDtoIn updateDtoIn) {
        Employee employee = employeeReadService.getById(updateDtoIn.getId());
        employeeMapper.enrichWith(updateDtoIn, employee);
        handleEmployeeRelations(employee, updateDtoIn.getManagerId(), updateDtoIn.getDepartmentId());
        EmployeeDtoOut employeeDtoOut = employeeMapper.to(employeeRepository.save(employee));
        notificationProducer.sendEmployeeUpdate(employeeDtoOut);
        return employeeDtoOut;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Employee employee = employeeReadService.getById(id);
        employee.setDeleted(true);
        employeeRepository.save(employee);
        phoneService.deletePhones(employee.getPhones());
        handleManagerDelete(employee);
        notificationProducer.sendEmployeeDelete(id);
    }

    @Override
    public void deleteDepartmentReference(Long departmentId) {
        employeeRepository.updateDepartmentToNullByDepartmentId(departmentId);
    }

    private void handleManagerDelete(Employee employee) {
        if (employee.getRole().equals(EmployeeRole.MANAGER)) {
            employeeRepository.updateManagerToNullForSubordinates(employee.getId());
        }
    }

    private void handleEmployeePhones(Employee employee, Set<PhoneDtoIn> phones) {
        if (phones != null && !phones.isEmpty()) {
            phoneService.validatePhonesCreate(phones.stream().map(PhoneDtoIn::getPhone).toList());
            employee.getPhones().forEach(phone -> phone.setEmployee(employee));
        }
    }

    private void handleEmployeeRelations(Employee employee, Long managerId, Long departmentId) {
        setEmployeeDepartment(employee, departmentId);
        setEmployeeManager(employee, managerId);
    }

    private void setEmployeeManager(Employee employee, Long managerId) {
        if (managerId == null) {
            employee.setManager(null);
        } else {
            checkDefaultEmployeeRole(employee);
            Employee manager = employeeReadService.getById(managerId);
            checkManagerRole(manager);
            checkDepartmentCompatibility(employee, manager);
            employee.setManager(manager);
        }
    }

    private void checkDefaultEmployeeRole(Employee employee) {
        if (!employee.getRole().equals(EmployeeRole.DEFAULT)) {
            throw new IllegalArgumentException(messageService.getMessage(
                    "exception.manager-for-not-default-employee"));
        }
    }

    private void checkManagerRole(Employee manager) {
        if (!manager.getRole().equals(EmployeeRole.MANAGER)) {
            throw new IllegalArgumentException(messageService.getMessage(
                    "exception.invalid-manager-id", manager.getId().toString()));
        }
    }

    private void checkDepartmentCompatibility(Employee employee, Employee manager) {
        if (!employee.getDepartmentId().equals(manager.getDepartmentId())) {
            throw new IllegalArgumentException(messageService.getMessage(
                    "exception.manager-employee-department-incompatibility"));
        }
    }

    private void setEmployeeDepartment(Employee employee, Long departmentId) {
        if (departmentId == null) {
            employee.setDepartmentId(null);
        } else {
            checkDepartment(departmentId);
            if (employee.getRole().equals(EmployeeRole.HEAD) && employeeRepository.
                    existsByRoleAndDepartmentIdAndDeletedFalse(EmployeeRole.HEAD, departmentId)) {
                throw new DataIntegrityViolationException(messageService.getMessage("exception.department-head-already-exists"));
            }
            employee.setDepartmentId(departmentId);
        }
    }

    private DepartmentDtoOut getDepartmentOrNull(Long departmentId) {
        DepartmentDtoOut departmentDtoOut = null;
        try {
            departmentDtoOut = departmentFeignClient.getDepartmentById(departmentId);
        } catch (RetryableException e) {
            log.error("Retryable exception occurred while fetching department with ID {}: {}", departmentId, e.getMessage());
        } catch (FeignException e) {
            String message = getResponseMessageFromFeignException(e);
            log.error(message, e.getMessage(), e);
        } catch (Exception e) {
            log.error(messageService.getMessage("exception.resource-failed-on-fetching-department",
                    departmentId.toString()), e.getMessage(), e);
        }
        return departmentDtoOut;
    }

    private void checkDepartment(Long departmentId) {
        try {
            departmentFeignClient.getDepartmentById(departmentId);
        } catch (RetryableException e) {
            log.error("Retryable exception occurred while checking department with ID {}: {}", departmentId, e.getMessage());
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
}

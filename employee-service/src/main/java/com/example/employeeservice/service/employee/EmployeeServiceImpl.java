package com.example.employeeservice.service.employee;

import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.out.EmployeeFullDtoOut;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.enums.EmployeeRole;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.employee.retriever.EmployeeRetrieverService;
import com.example.employeeservice.service.message.MessageService;
import com.example.employeeservice.service.phone.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRetrieverService employeeRetrieverService;

    private final PhoneService phoneService;

    private final MessageService messageService;

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDtoOut> getEmployeesDto(Pageable pageable) {
        // TODO: filter and sorting logic
        return employeeRepository.findAllByNotDeleted(pageable).map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDtoOut getEmployeeDto(Long id) {
        return employeeMapper.toDto(employeeRetrieverService.getEmployeeById(id));
    }

    @Transactional
    @Override
    public EmployeeDtoOut createEmployee(EmployeeCreateDtoIn createDtoIn) {
        Employee employee = employeeMapper.fromDto(createDtoIn);
        handleEmployeePhones(employee, createDtoIn.getPhones());
        handleEmployeeRelations(employee, createDtoIn.getManagerId(), createDtoIn.getDepartmentId());
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeDtoOut updateEmployee(EmployeeUpdateDtoIn updateDtoIn) {
        Employee employee = employeeRetrieverService.getEmployeeById(updateDtoIn.getId());
        employeeMapper.updateFromDto(updateDtoIn, employee);
        handleEmployeeRelations(employee, updateDtoIn.getManagerId(), updateDtoIn.getDepartmentId());
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRetrieverService.getEmployeeById(id);
        employee.setDeleted(true);
        employeeRepository.save(employee);
        phoneService.deletePhones(employee.getPhones());
        handleManagerDelete(employee);
    }

    private void handleManagerDelete(Employee manager) {
        employeeRepository.updateManagerToNullForSubordinates(manager.getId());
    }

    private void handleEmployeePhones(Employee employee, Set<PhoneDtoIn> phones) {
        if (phones != null && !phones.isEmpty()) {
            phoneService.validatePhonesCreate(phones.stream().map(PhoneDtoIn::getPhone).toList());
            employee.getPhones().forEach(phone -> phone.setEmployee(employee));
        }
    }

    private void handleEmployeeRelations(Employee employee, Long managerId, Long departmentId) {
        setEmployeeManager(employee, managerId);
        setEmployeeDepartment(employee, departmentId);
    }

    private void setEmployeeManager(Employee employee, Long managerId) {
        if (managerId == null) {
            employee.setManager(null);
        } else {
            if (!employee.getRole().equals(EmployeeRole.DEFAULT)) {
                throw new IllegalArgumentException(messageService.getMessage(
                        "exception.manager-for-not-default-employee"));
            }
            Employee manager = employeeRetrieverService.getEmployeeById(managerId);
            if (!manager.getRole().equals(EmployeeRole.MANAGER)) {
                throw new IllegalArgumentException(messageService.getMessage(
                        "exception.invalid.manager.id", manager.getId().toString()));
            }
            employee.setManager(manager);
        }
    }

    private void setEmployeeDepartment(Employee employee, Long departmentId) {
        // TODO: check if department exists using Feign Client
        employee.setDepartmentId(departmentId);
    }

    private EmployeeFullDtoOut toFullDtoOut(Employee employee) {
        // TODO: prepare full dto out using feign client
        return null;
    }
}

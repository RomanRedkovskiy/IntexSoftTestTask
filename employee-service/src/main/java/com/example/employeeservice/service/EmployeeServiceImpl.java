package com.example.employeeservice.service;

import com.example.employeeservice.dto.employee.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.EmployeeFullDtoOut;
import com.example.employeeservice.dto.employee.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoOut;
import com.example.employeeservice.dto.phone.PhoneFullDtoIn;
import com.example.employeeservice.dto.phone.PhoneFullDtoOut;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.mapper.PhoneMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper;

    @Override
    public Page<EmployeeDtoOut> getEmployeesDto(int page, int size) {
        // TODO: filter and sorting logic
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAllByNotDeleted(pageable).map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDtoOut getEmployeeDto(Long id) {
        return employeeMapper.toDto(getEmployeeById(id));
    }

    @Override
    public PhoneFullDtoOut getPhoneDtoById(Long id) {
        Phone phone = getPhoneById(id);
        return phoneMapper.toFullDto(phone);
    }

    @Override
    public List<PhoneDtoOut> getPhonesByEmployeeId(Long employeeId) {
        return phoneRepository.findByEmployeeId(employeeId).stream().map(phoneMapper::toDto).toList();
    }

    @Transactional
    @Override
    public EmployeeDtoOut createEmployee(EmployeeCreateDtoIn employeeCreateDtoIn) {
        // TODO: validate phones using libPhoneNumbers (validatePhones(employeeCreateDtoIn.getPhones()))
        checkPhoneUniqueness(employeeCreateDtoIn.getPhones());
        Employee employee = createEmployeeByDtoIn(employeeCreateDtoIn);
        phoneRepository.saveAll(employeeCreateDtoIn.getPhones().stream().map(
                dtoIn -> phoneMapper.fromDtoAndEmployee(dtoIn, employee)).toList());
        return employeeMapper.toDto(employee);
    }

    private void checkPhoneUniqueness(Set<PhoneDtoIn> phones) {
        List<String> duplicatedPhones = phoneRepository.findByPhoneList(phones.stream()
                .map(PhoneDtoIn::getPhone).toList());
        if (!duplicatedPhones.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate phone numbers: " +
                    String.join(", ", duplicatedPhones));
        }
    }

    private Employee createEmployeeByDtoIn(EmployeeCreateDtoIn employeeCreateDtoIn) {
        Employee employee = employeeMapper.fromDto(employeeCreateDtoIn);
        employee.setManager(getEmployeeById(employeeCreateDtoIn.getManagerId()));
        // TODO: check if department exists using Feign Client + other checks
        employee.setDepartmentId(employeeCreateDtoIn.getDepartmentId());
        employee.setRegistrationTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        return employeeRepository.save(employee);
    }

    @Override
    public PhoneFullDtoOut createPhone(PhoneFullDtoIn phoneDtoIn) {
        getEmployeeById(phoneDtoIn.getEmployeeId()); // Checks if employee exists
        Phone phone = phoneMapper.fromFullDto(phoneDtoIn);
        return phoneMapper.toFullDto(phoneRepository.save(phone));
    }

    @Override
    public EmployeeDtoOut updateEmployee(EmployeeUpdateDtoIn employeeUpdateDtoIn) {
        Employee employee = getEmployeeById(employeeUpdateDtoIn.getId());
        // TODO: checks
        employeeMapper.updateFromDto(employeeUpdateDtoIn, employee);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Override
    public PhoneFullDtoOut updatePhone(PhoneFullDtoIn phoneDtoIn) {
        getEmployeeById(phoneDtoIn.getEmployeeId()); // Checks if employee exists
        Phone phone = phoneMapper.fromFullDto(phoneDtoIn);
        return phoneMapper.toFullDto(phoneRepository.save(phone));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    @Override
    public void deletePhone(Long id) {
        phoneRepository.delete(getPhoneById(id));
    }

    private Employee getEmployeeById(Long id) {
        return employeeRepository.findByIdAndNotDeleted(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with given ID not found"));
    }

    private Phone getPhoneById(Long id) {
        return phoneRepository.findByIdWithEmployee(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone with given ID not found"));
    }

    private EmployeeFullDtoOut toFullDtoOut(Employee employee) {
        // TODO: prepare full dto out using feign client
        return null;
    }
}

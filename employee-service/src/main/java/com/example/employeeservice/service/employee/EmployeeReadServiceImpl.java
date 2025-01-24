package com.example.employeeservice.service.employee;

import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeReadServiceImpl implements EmployeeReadService {

    private final MessageService messageService;

    private final EmployeeRepository employeeRepository;

    public Employee getById(Long id) {
        return employeeRepository.findByIdAndDeletedFalse(id).orElseThrow(() ->
                new EntityNotFoundException(messageService.getMessage("exception.employee-not-found", id.toString())));
    }
}

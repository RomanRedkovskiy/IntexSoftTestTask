package com.example.employeeservice.service.employee;

import com.example.employeeservice.dto.department.DepartmentDtoOut;
import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.in.EmployeeUpdateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.employee.out.EmployeeFullDtoOut;
import com.example.employeeservice.feign.DepartmentFeignClient;
import com.example.employeeservice.kafka.NotificationProducer;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.enums.EmployeeRole;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.message.MessageService;
import com.example.employeeservice.service.phone.PhoneService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeReadService employeeReadService;

    @Mock
    private PhoneService phoneService;

    @Mock
    private MessageService messageService;

    @Mock
    private DepartmentFeignClient departmentFeignClient;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployeesDto_WhenValidEmployees_ThenReturnsEmployeesList() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Employee employee = new Employee();
        EmployeeDtoOut employeeDtoOut = new EmployeeDtoOut();
        when(employeeRepository.findAllByDeletedFalse(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(employee)));
        when(employeeMapper.to(employee)).thenReturn(employeeDtoOut);

        // Act
        Page<EmployeeDtoOut> result = employeeService.getDtoPage(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository).findAllByDeletedFalse(pageable);
        verify(employeeMapper).to(employee);
    }

    @Test
    void getEmployeeDto_WhenValidEmployee_ThenReturnsEmployee() {
        // Arrange
        Long employeeId = 1L;
        Long departmentId = 2L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDepartmentId(departmentId);

        DepartmentDtoOut departmentDtoOut = new DepartmentDtoOut();
        departmentDtoOut.setId(departmentId);

        EmployeeFullDtoOut employeeFullDtoOut = new EmployeeFullDtoOut();

        when(employeeReadService.getById(employeeId)).thenReturn(employee);
        when(departmentFeignClient.getDepartmentById(departmentId)).thenReturn(departmentDtoOut);
        when(employeeMapper.to(employee, departmentDtoOut)).thenReturn(employeeFullDtoOut);

        // Act
        EmployeeFullDtoOut result = employeeService.getDto(employeeId);

        // Assert
        assertNotNull(result);
        verify(employeeReadService).getById(employeeId);
        verify(departmentFeignClient).getDepartmentById(departmentId);
        verify(employeeMapper).to(employee, departmentDtoOut);
    }

    @Test
    void getEmployeeDto_WhenInvalidEmployee_ThenThrowsException() {
        // Arrange
        Long employeeId = 1L;
        when(employeeReadService.getById(employeeId)).thenThrow(new EntityNotFoundException("Employee not found"));

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> employeeService.getDto(employeeId));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeReadService).getById(employeeId);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void createEmployee_WhenValidEmployee_ThenReturnsCreatedEmployee() {
        // Arrange
        EmployeeCreateDtoIn createDtoIn = new EmployeeCreateDtoIn();
        createDtoIn.setPhones(Set.of());
        Employee employee = new Employee();
        employee.setRole(EmployeeRole.DEFAULT);
        EmployeeDtoOut employeeDtoOut = new EmployeeDtoOut();
        when(employeeMapper.from(createDtoIn)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.to(employee)).thenReturn(employeeDtoOut);

        // Act
        EmployeeDtoOut result = employeeService.create(createDtoIn);

        // Assert
        assertNotNull(result);
        verify(employeeMapper).from(createDtoIn);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).to(employee);
    }

    @Test
    void createEmployee_WhenInvalidManagerId_ThenThrowException() {
        // Arrange
        EmployeeCreateDtoIn createDtoIn = new EmployeeCreateDtoIn();
        createDtoIn.setManagerId(99L);
        Employee employee = new Employee();
        employee.setRole(EmployeeRole.DEFAULT);

        when(employeeMapper.from(createDtoIn)).thenReturn(employee);
        when(employeeReadService.getById(createDtoIn.getManagerId())).thenThrow(
                new EntityNotFoundException("Invalid manager ID"));

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> employeeService.create(createDtoIn));
        assertEquals("Invalid manager ID", exception.getMessage());
        verify(employeeReadService).getById(createDtoIn.getManagerId());
    }

    @Test
    void createEmployee_WhenManagerNotManagerRole_ThenThrowException() {
        // Arrange
        EmployeeCreateDtoIn createDtoIn = new EmployeeCreateDtoIn();
        createDtoIn.setManagerId(2L);

        Employee employee = new Employee();
        employee.setRole(EmployeeRole.DEFAULT);

        Employee manager = new Employee();
        manager.setId(2L);
        manager.setRole(EmployeeRole.DEFAULT); // Invalid role

        when(employeeMapper.from(createDtoIn)).thenReturn(employee);
        when(employeeReadService.getById(createDtoIn.getManagerId())).thenReturn(manager);
        when(messageService.getMessage("exception.invalid-manager-id", "2")).thenReturn("Invalid manager ID: 2");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.create(createDtoIn));
        assertEquals("Invalid manager ID: 2", exception.getMessage());
        verify(employeeReadService).getById(createDtoIn.getManagerId());
        verify(messageService).getMessage("exception.invalid-manager-id", "2");
    }

    @Test
    void updateEmployee_WhenValidEmployee_ThenReturnsUpdatedEmployee() {
        // Arrange
        EmployeeUpdateDtoIn updateDtoIn = new EmployeeUpdateDtoIn();
        updateDtoIn.setId(1L);
        Employee employee = new Employee();
        employee.setRole(EmployeeRole.DEFAULT);
        EmployeeDtoOut employeeDtoOut = new EmployeeDtoOut();
        when(employeeReadService.getById(updateDtoIn.getId())).thenReturn(employee);
        doNothing().when(employeeMapper).enrichWith(updateDtoIn, employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.to(employee)).thenReturn(employeeDtoOut);

        // Act
        EmployeeDtoOut result = employeeService.update(updateDtoIn);

        // Assert
        assertNotNull(result);
        verify(employeeReadService).getById(updateDtoIn.getId());
        verify(employeeMapper).enrichWith(updateDtoIn, employee);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).to(employee);
    }

    @Test
    void updateEmployee_WhenInvalidRoleForManager_ThenThrowException() {
        // Arrange
        EmployeeUpdateDtoIn updateDtoIn = new EmployeeUpdateDtoIn();
        updateDtoIn.setId(1L);
        updateDtoIn.setManagerId(2L);

        Employee employee = new Employee();
        employee.setRole(EmployeeRole.HEAD);

        when(employeeReadService.getById(updateDtoIn.getId())).thenReturn(employee);
        when(messageService.getMessage("exception.manager-for-not-default-employee")).thenReturn("Cannot assign manager for non-default employee role");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.update(updateDtoIn));
        assertEquals("Cannot assign manager for non-default employee role", exception.getMessage());
        verify(messageService).getMessage("exception.manager-for-not-default-employee");
    }

    @Test
    void deleteEmployee_WhenValidEmployee_ThenDeletesEmployee() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setRole(EmployeeRole.DEFAULT);
        when(employeeReadService.getById(employeeId)).thenReturn(employee);

        // Act
        employeeService.delete(employeeId);

        // Assert
        assertTrue(employee.getDeleted());
        verify(employeeReadService).getById(employeeId);
        verify(employeeRepository).save(employee);
        verify(phoneService).deletePhones(employee.getPhones());
    }

    @Test
    void deleteEmployee_WhenEmployeeNotFound_ThenThrowException() {
        // Arrange
        Long employeeId = 1L;
        when(employeeReadService.getById(employeeId)).thenThrow(new EntityNotFoundException("Employee not found"));

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> employeeService.delete(employeeId));
        assertEquals("Employee not found", exception.getMessage());
        verify(employeeReadService).getById(employeeId);
        verifyNoInteractions(phoneService, employeeRepository);
    }
}

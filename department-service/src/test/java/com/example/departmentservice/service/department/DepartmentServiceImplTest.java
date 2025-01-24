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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DepartmentServiceImplTest {

    @Mock
    private MessageService messageService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private EmployeeFeignClient employeeFeignClient;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDepartmentsDto_WhenValidDepartments_ThenReturnsDepartmentsList() {
        // Arrange
        Department department = new Department();
        department.setId(1L);
        DepartmentDtoOut departmentDtoOut = new DepartmentDtoOut();
        departmentDtoOut.setId(1L);

        when(departmentRepository.findAllByDeletedFalse()).thenReturn(List.of(department));
        when(departmentMapper.to(department)).thenReturn(departmentDtoOut);

        // Act
        List<DepartmentDtoOut> result = departmentService.getDtoList();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(departmentRepository).findAllByDeletedFalse();
        verify(departmentMapper).to(department);
    }

    @Test
    void getDepartmentDto_WhenValidDepartment_ThenReturnsDepartmentDetails() {
        // Arrange
        Long departmentId = 1L;

        // Mock the Department entity
        Department department = new Department();
        department.setId(departmentId);

        // Mock the DepartmentDtoOut and DepartmentFullDtoOut
        DepartmentFullDtoOut departmentFullDtoOut = new DepartmentFullDtoOut();
        departmentFullDtoOut.setId(departmentId);

        // Mock the EmployeeDtoOut list
        List<EmployeeDtoOut> employees = List.of(new EmployeeDtoOut(), new EmployeeDtoOut());

        // Mock repository and Feign client behavior
        when(departmentRepository.findByIdAndDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(employeeFeignClient.getEmployeesByDepartmentId(departmentId)).thenReturn(employees);

        // Mock the mapping logic
        when(departmentMapper.to(department, employees)).thenReturn(departmentFullDtoOut);

        // Act
        DepartmentFullDtoOut result = departmentService.getDto(departmentId);

        // Assert
        assertNotNull(result);
        assertEquals(departmentId, result.getId());
        verify(departmentRepository).findByIdAndDeletedFalse(departmentId);
        verify(employeeFeignClient).getEmployeesByDepartmentId(departmentId);
        verify(departmentMapper).to(department, employees);
    }


    @Test
    void getDepartmentDto_WhenNonExistentDepartmentId_ThenThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistentId = 1L;
        when(departmentRepository.findByIdAndDeletedFalse(nonExistentId))
                .thenReturn(Optional.empty());
        when(messageService.getMessage("exception.department-not-found", nonExistentId))
                .thenReturn("Department not found for ID: " + nonExistentId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> departmentService.getDto(nonExistentId));

        verify(departmentRepository).findByIdAndDeletedFalse(nonExistentId);
        verify(messageService).getMessage("exception.department-not-found", nonExistentId);
        assert exception.getMessage().equals("Department not found for ID: " + nonExistentId);
    }

    @Test
    void createDepartment_WhenValidDepartment_ThenReturnsCreatedDepartment() {
        // Arrange
        DepartmentCreateDtoIn createDto = new DepartmentCreateDtoIn();
        Department department = new Department();
        DepartmentDtoOut departmentDtoOut = new DepartmentDtoOut();

        when(departmentMapper.from(createDto)).thenReturn(department);
        when(departmentMapper.to(department)).thenReturn(departmentDtoOut);

        // Act
        DepartmentDtoOut result = departmentService.create(createDto);

        // Assert
        assertNotNull(result);
        verify(departmentMapper).from(createDto);
        verify(departmentRepository).save(department);
        verify(departmentMapper).to(department);
    }

    @Test
    void updateDepartment_WhenValidDepartment_ThenReturnsUpdatedDepartment() {
        // Arrange
        DepartmentUpdateDtoIn updateDto = new DepartmentUpdateDtoIn();
        updateDto.setId(1L);
        Department department = new Department();
        DepartmentDtoOut departmentDtoOut = new DepartmentDtoOut();

        when(departmentRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.to(department)).thenReturn(departmentDtoOut);

        // Act
        DepartmentDtoOut result = departmentService.update(updateDto);

        // Assert
        assertNotNull(result);
        verify(departmentRepository).findByIdAndDeletedFalse(1L);
        verify(departmentMapper).enrichWith(updateDto, department);
        verify(departmentRepository).save(department);
        verify(departmentMapper).to(department);
    }

    @Test
    void updateDepartment_WhenNonExistentDepartmentId_ThenThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistentId = 2L;
        DepartmentUpdateDtoIn updateDtoIn = new DepartmentUpdateDtoIn();
        updateDtoIn.setId(nonExistentId);

        when(departmentRepository.findByIdAndDeletedFalse(nonExistentId))
                .thenReturn(Optional.empty());
        when(messageService.getMessage("exception.department-not-found", nonExistentId))
                .thenReturn("Department not found for ID: " + nonExistentId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> departmentService.update(updateDtoIn));

        verify(departmentRepository).findByIdAndDeletedFalse(nonExistentId);
        verify(messageService).getMessage("exception.department-not-found", nonExistentId);
        assert exception.getMessage().equals("Department not found for ID: " + nonExistentId);
    }

    @Test
    void deleteDepartment_WhenValidDepartment_ThenDeletesDepartment() {
        // Arrange
        Department department = new Department();
        department.setId(1L);

        when(departmentRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(department));

        // Act
        departmentService.delete(1L);

        // Assert
        assertTrue(department.getDeleted());
        verify(departmentRepository).findByIdAndDeletedFalse(1L);
        verify(departmentRepository).save(department);
    }

    @Test
    void deleteDepartment_WhenNonExistentDepartmentId_ThenThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistentId = 3L;

        when(departmentRepository.findByIdAndDeletedFalse(nonExistentId))
                .thenReturn(Optional.empty());
        when(messageService.getMessage("exception.department-not-found", nonExistentId))
                .thenReturn("Department not found for ID: " + nonExistentId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> departmentService.delete(nonExistentId));

        verify(departmentRepository).findByIdAndDeletedFalse(nonExistentId);
        verify(messageService).getMessage("exception.department-not-found", nonExistentId);
        assert exception.getMessage().equals("Department not found for ID: " + nonExistentId);
    }
}

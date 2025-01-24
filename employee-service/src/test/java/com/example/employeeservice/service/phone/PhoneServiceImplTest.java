package com.example.employeeservice.service.phone;

import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneUpdateDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.kafka.NotificationProducer;
import com.example.employeeservice.mapper.PhoneMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;
import com.example.employeeservice.repository.PhoneRepository;
import com.example.employeeservice.service.employee.EmployeeReadService;
import com.example.employeeservice.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PhoneServiceImplTest {

    @Mock
    private MessageService messageService;

    @Mock
    private EmployeeReadService employeeReadService;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDtoById_WhenValidDataProvided_ThenReturnsDtoDetails() {
        Long phoneId = 1L;
        Phone phone = new Phone();
        phone.setId(phoneId);
        PhoneDtoOut expectedDto = new PhoneDtoOut();

        when(phoneRepository.findByIdAndDeletedFalse(phoneId)).thenReturn(Optional.of(phone));
        when(phoneMapper.to(phone)).thenReturn(expectedDto);

        PhoneDtoOut result = phoneService.getDtoById(phoneId);

        assertEquals(expectedDto, result);
        verify(phoneRepository).findByIdAndDeletedFalse(phoneId);
        verify(phoneMapper).to(phone);
    }

    @Test
    void getPhoneDtoById_WhenIdIsInvalid_ThenThrowsEntityNotFoundException() {
        Long phoneId = 1L;
        when(phoneRepository.findByIdAndDeletedFalse(phoneId)).thenReturn(Optional.empty());
        when(messageService.getMessage("exception.phone-not-found", phoneId.toString()))
                .thenReturn("Phone not found");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> phoneService.getDtoById(phoneId));

        assertEquals("Phone not found", exception.getMessage());
    }

    @Test
    void getPhonesByEmployeeId_WhenValidDataProvided_ThenReturnsEmployeePhones() {
        Long employeeId = 1L;
        Phone phone = new Phone();
        PhoneDtoOut dtoOut = new PhoneDtoOut();

        when(employeeReadService.getById(employeeId)).thenReturn(new Employee());
        when(phoneRepository.findByEmployeeIdAndDeletedFalseAndEmployeeDeletedFalse(employeeId)).thenReturn(List.of(phone));
        when(phoneMapper.to(phone)).thenReturn(dtoOut);

        List<PhoneDtoOut> result = phoneService.getListByEmployeeId(employeeId);

        assertEquals(1, result.size());
        assertEquals(dtoOut, result.get(0));
        verify(employeeReadService).getById(employeeId);
        verify(phoneRepository).findByEmployeeIdAndDeletedFalseAndEmployeeDeletedFalse(employeeId);
        verify(phoneMapper).to(phone);
    }

    @Test
    void createPhone_WhenValidDataProvided_ThenReturnsCreatedPhone() {
        PhoneCreateDtoIn dtoIn = new PhoneCreateDtoIn();
        dtoIn.setPhone("+375296001327");
        dtoIn.setEmployeeId(1L);
        Employee employee = new Employee();
        Phone phone = new Phone();
        PhoneDtoOut expectedDto = new PhoneDtoOut();

        when(employeeReadService.getById(dtoIn.getEmployeeId())).thenReturn(employee);
        when(phoneMapper.from(dtoIn, employee)).thenReturn(phone);
        when(phoneRepository.save(phone)).thenReturn(phone);
        when(phoneMapper.to(phone)).thenReturn(expectedDto);

        PhoneDtoOut result = phoneService.create(dtoIn);

        assertEquals(expectedDto, result);
        verify(employeeReadService).getById(dtoIn.getEmployeeId());
        verify(phoneMapper).from(dtoIn, employee);
        verify(phoneRepository).save(phone);
        verify(phoneMapper).to(phone);
    }

    @Test
    void createPhone_WhenPhoneDuplicates_ThenThrowsDataIntegrityViolationException() {
        PhoneCreateDtoIn phoneDtoIn = new PhoneCreateDtoIn();
        phoneDtoIn.setPhone("+375297891223");
        phoneDtoIn.setEmployeeId(1L);

        when(phoneRepository.findByPhoneIn(List.of("+375297891223"))).thenReturn(List.of("+375297891223"));
        when(messageService.getMessage("exception.duplicate-phone-numbers", "+375297891223"))
                .thenReturn("Duplicate phone number: +375297891223");

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> phoneService.create(phoneDtoIn));

        assertEquals("Duplicate phone number: +375297891223", exception.getMessage());
    }

    @Test
    void updatePhone_WhenValidDataProvided_ThenReturnsUpdatedPhone() {
        PhoneUpdateDtoIn dtoIn = new PhoneUpdateDtoIn();
        dtoIn.setId(1L);
        dtoIn.setPhone("+375447085291");
        dtoIn.setEmployeeId(1L);
        Phone phone = new Phone();
        phone.setPhone("+375447085291");
        Employee employee = new Employee();
        PhoneDtoOut expectedDto = new PhoneDtoOut();

        when(phoneRepository.findByIdAndDeletedFalse(dtoIn.getId())).thenReturn(Optional.of(phone));
        when(employeeReadService.getById(dtoIn.getEmployeeId())).thenReturn(employee);
        doNothing().when(phoneMapper).enrichWith(phone, dtoIn, employee);
        when(phoneRepository.save(phone)).thenReturn(phone);
        when(phoneMapper.to(phone)).thenReturn(expectedDto);

        PhoneDtoOut result = phoneService.update(dtoIn);

        assertEquals(expectedDto, result);
        verify(phoneRepository).findByIdAndDeletedFalse(dtoIn.getId());
        verify(employeeReadService).getById(dtoIn.getEmployeeId());
        verify(phoneMapper).enrichWith(phone, dtoIn, employee);
        verify(phoneRepository).save(phone);
        verify(phoneMapper).to(phone);
    }

    @Test
    void updatePhone_WhenPhoneIsInvalid_ThenThrowsEntityNotFoundException() {
        PhoneUpdateDtoIn phoneDtoIn = new PhoneUpdateDtoIn();
        phoneDtoIn.setId(1L);

        when(phoneRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        when(messageService.getMessage("exception.phone-not-found", "1"))
                .thenReturn("Phone not found");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> phoneService.update(phoneDtoIn));

        assertEquals("Phone not found", exception.getMessage());
    }

    @Test
    void updatePhone_WhenPhoneDuplicates_ThenThrowsDataIntegrityViolationException() {
        Phone existingPhone = new Phone();
        existingPhone.setId(1L);
        existingPhone.setPhone("+375253456789");

        PhoneUpdateDtoIn phoneDtoIn = new PhoneUpdateDtoIn();
        phoneDtoIn.setId(1L);
        phoneDtoIn.setPhone("+375257654321");

        when(phoneRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(existingPhone));
        when(phoneRepository.existsByPhoneAndIdNot("+375257654321", 1L)).thenReturn(true);
        when(messageService.getMessage("exception.duplicate-phone-number", "+375257654321"))
                .thenReturn("Duplicate phone number: +375257654321");

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> phoneService.update(phoneDtoIn));

        assertEquals("Duplicate phone number: +375257654321", exception.getMessage());
    }

    @Test
    void savePhones_WhenValidDataProvided_ThenReturnsSavedPhones() {
        Employee employee = new Employee();
        PhoneDtoIn dtoIn = new PhoneDtoIn();
        Phone phone = new Phone();

        when(phoneMapper.from(dtoIn, employee)).thenReturn(phone);
        when(phoneRepository.saveAll(anyList())).thenReturn(List.of(phone));

        List<Phone> result = phoneService.saveList(Set.of(dtoIn), employee);

        assertEquals(1, result.size());
        assertEquals(phone, result.get(0));
        verify(phoneMapper).from(dtoIn, employee);
        verify(phoneRepository).saveAll(anyList());
    }

    @Test
    void validatePhonesCreate_WhenValidDataProvided_ThenReturnsValidPhones() {
        List<String> phones = List.of("123456789", "987654321");

        when(phoneRepository.findByPhoneIn(phones)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> phoneService.validatePhonesCreate(phones));
        verify(phoneRepository).findByPhoneIn(phones);
    }

    @Test
    void deletePhone_WhenPhoneIdIsInvalid_ThenThrowsEntityNotFoundException() {
        Long phoneId = 1L;

        when(phoneRepository.findByIdAndDeletedFalse(phoneId)).thenReturn(Optional.empty());
        when(messageService.getMessage("exception.phone-not-found", phoneId.toString()))
                .thenReturn("Phone not found");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> phoneService.deleteById(phoneId));

        assertEquals("Phone not found", exception.getMessage());
    }

    @Test
    void deletePhone_WhenValidDataProvided_ThenDeletesPhone() {
        Long phoneId = 1L;
        Phone phone = new Phone();
        phone.setId(phoneId);

        when(phoneRepository.findByIdAndDeletedFalse(phoneId)).thenReturn(Optional.of(phone));
        when(phoneRepository.save(phone)).thenReturn(phone);

        phoneService.deleteById(phoneId);

        assertTrue(phone.getDeleted());
        verify(phoneRepository).save(phone);
    }

    @Test
    void deletePhones_WhenValidDataProvided_ThenDeletesPhones() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        Set<Phone> phones = Set.of(phone1, phone2);

        when(phoneRepository.saveAll(phones)).thenReturn(List.of(phone1, phone2));

        phoneService.deletePhones(phones);

        assertTrue(phone1.getDeleted());
        assertTrue(phone2.getDeleted());
        verify(phoneRepository).saveAll(phones);
    }

}
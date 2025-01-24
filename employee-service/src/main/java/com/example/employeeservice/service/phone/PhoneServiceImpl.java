package com.example.employeeservice.service.phone;

import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneUpdateDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.mapper.PhoneMapper;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;
import com.example.employeeservice.repository.PhoneRepository;
import com.example.employeeservice.service.employee.retriever.EmployeeRetrieverService;
import com.example.employeeservice.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final MessageService messageService;

    private final EmployeeRetrieverService employeeRetrieverService;

    private final PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper;

    @Override
    public PhoneDtoOut getPhoneDtoById(Long id) {
        Phone phone = getPhoneById(id);
        return phoneMapper.toDto(phone);
    }

    @Override
    public List<PhoneDtoOut> getPhonesByEmployeeId(Long employeeId) {
        // throws EntityNotFoundException if no present employee with this id
        employeeRetrieverService.getEmployeeById(employeeId);
        return phoneRepository.findByEmployeeId(employeeId).stream().map(this::toDto).toList();
    }

    @Override
    public PhoneDtoOut createPhone(PhoneCreateDtoIn phoneDtoIn) {
        validatePhonesCreate(Collections.singletonList(phoneDtoIn.getPhone()));
        Phone phone = fromCreateDtoIn(phoneDtoIn);
        return phoneMapper.toDto(phoneRepository.save(phone));
    }

    @Override
    public PhoneDtoOut updatePhone(PhoneUpdateDtoIn phoneDtoIn) {
        Phone phone = getPhoneById(phoneDtoIn.getId());
        validatePhoneUpdate(phone, phoneDtoIn.getPhone());
        fromUpdateDtoIn(phone, phoneDtoIn);
        return phoneMapper.toDto(phoneRepository.save(phone));
    }

    @Override
    public List<Phone> savePhones(Set<PhoneDtoIn> phones, Employee employee) {
        return phoneRepository.saveAll(phones.stream().map(
                dtoIn -> phoneMapper.fromDtoAndEmployee(dtoIn, employee)).toList());
    }

    @Override
    public void deletePhone(Long id) {
        Phone phone = getPhoneById(id);
        phone.setDeleted(true);
        phoneRepository.save(phone);
    }

    @Override
    public void deletePhones(Set<Phone> phones) {
        if (phones != null && !phones.isEmpty()) {
            phones.forEach(phone -> phone.setDeleted(true));
            phoneRepository.saveAll(phones);
        }
    }

    @Override
    public void validatePhonesCreate(List<String> phones) {
        if (phones != null && !phones.isEmpty()) {
            // TODO: validate phones using libPhoneNumbers (validatePhones(employeeCreateDtoIn.getPhones()))
            checkPhonesUniqueness(phones);
        }
    }

    private void validatePhoneUpdate(Phone oldPhone, String newPhone) {
        // TODO: validate phones using libPhoneNumbers (validatePhones(employeeCreateDtoIn.getPhones()))
       if (!oldPhone.getPhone().equals(newPhone) && phoneRepository.existsByPhoneAndIdNot(newPhone, oldPhone.getId())) {
           throw new DataIntegrityViolationException(messageService.getMessage(
                   "exception.duplicate-phone-number", newPhone));
       }
    }

    private void checkPhonesUniqueness(List<String> phones) {
        List<String> duplicatedPhones = phoneRepository.findByPhoneList(phones);
        if (!duplicatedPhones.isEmpty()) {
            throw new DataIntegrityViolationException(messageService.getMessage(
                    "exception.duplicate-phone-numbers", String.join(", ", duplicatedPhones)));
        }
    }

    @Override
    public PhoneDtoOut toDto(Phone phone) {
        return phoneMapper.toDto(phone);
    }

    private Phone getPhoneById(Long id) {
        return phoneRepository.findByIdWithEmployee(id).orElseThrow(() ->
                new EntityNotFoundException(messageService.getMessage("exception.phone-not-found", id.toString())));
    }

    private Phone fromCreateDtoIn(PhoneCreateDtoIn phoneDtoIn) {
        Employee employee = employeeRetrieverService.getEmployeeById(phoneDtoIn.getEmployeeId());
        return phoneMapper.fromCreateDtoAndEmployee(phoneDtoIn, employee);
    }

    private void fromUpdateDtoIn(Phone phone, PhoneUpdateDtoIn phoneDtoIn) {
        Employee employee = employeeRetrieverService.getEmployeeById(phoneDtoIn.getEmployeeId());
        phoneMapper.fromUpdateDtoAndEmployee(phone, phoneDtoIn, employee);
    }

}

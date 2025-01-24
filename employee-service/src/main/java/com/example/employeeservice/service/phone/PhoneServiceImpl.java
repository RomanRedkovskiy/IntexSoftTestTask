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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final MessageService messageService;

    private final EmployeeReadService employeeReadService;

    private final PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper;

    private final NotificationProducer notificationProducer;

    @Transactional(readOnly = true)
    @Override
    public PhoneDtoOut getDtoById(Long id) {
        Phone phone = getPhoneById(id);
        return phoneMapper.to(phone);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PhoneDtoOut> getListByEmployeeId(Long employeeId) {
        checkEmployeeById(employeeId);
        return phoneRepository.findByEmployeeIdAndDeletedFalseAndEmployeeDeletedFalse(employeeId).stream().map(this::toDto).toList();
    }

    @Transactional
    @Override
    public PhoneDtoOut create(PhoneCreateDtoIn phoneDtoIn) {
        validatePhonesCreate(Collections.singletonList(phoneDtoIn.getPhone()));
        Phone phone = fromCreateDtoIn(phoneDtoIn);
        PhoneDtoOut phoneDtoOut = phoneMapper.to(phoneRepository.save(phone));
        notificationProducer.sendPhoneCreate(phoneDtoOut);
        return phoneDtoOut;
    }

    @Transactional
    @Override
    public PhoneDtoOut update(PhoneUpdateDtoIn phoneDtoIn) {
        Phone phone = getPhoneById(phoneDtoIn.getId());
        validatePhoneUpdate(phone, phoneDtoIn.getPhone());
        fromUpdateDtoIn(phone, phoneDtoIn);
        PhoneDtoOut phoneDtoOut = phoneMapper.to(phoneRepository.save(phone));
        notificationProducer.sendPhoneUpdate(phoneDtoOut);
        return phoneDtoOut;
    }

    @Transactional
    @Override
    public List<Phone> saveList(Set<PhoneDtoIn> phoneDtoSet, Employee employee) {
        List<Phone> phones = phoneDtoSet.stream()
                .map(dtoIn -> phoneMapper.from(dtoIn, employee))
                .toList();
        return phoneRepository.saveAll(phones);
    }

    @Override
    public void deleteById(Long id) {
        Phone phone = getPhoneById(id);
        phone.setDeleted(true);
        phoneRepository.save(phone);
        notificationProducer.sendPhoneDelete(id);
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
            checkPhonesUniqueness(phones);
        }
    }

    @Override
    public PhoneDtoOut toDto(Phone phone) {
        return phoneMapper.to(phone);
    }

    private void validatePhoneUpdate(Phone oldPhone, String newPhone) {
       if (!oldPhone.getPhone().equals(newPhone) && phoneRepository.existsByPhoneAndIdNot(newPhone, oldPhone.getId())) {
           throw new DataIntegrityViolationException(messageService.getMessage(
                   "exception.duplicate-phone-number", newPhone));
       }
    }

    private void checkPhonesUniqueness(List<String> phones) {
        List<String> duplicatedPhones = phoneRepository.findByPhoneIn(phones);
        if (!duplicatedPhones.isEmpty()) {
            throw new DataIntegrityViolationException(messageService.getMessage(
                    "exception.duplicate-phone-numbers", String.join(", ", duplicatedPhones)));
        }
    }

    private void checkEmployeeById(Long employeeId) {
        employeeReadService.getById(employeeId);
    }

    private Phone getPhoneById(Long id) {
        return phoneRepository.findByIdAndDeletedFalse(id).orElseThrow(() ->
                new EntityNotFoundException(messageService.getMessage("exception.phone-not-found", id.toString())));
    }

    private Phone fromCreateDtoIn(PhoneCreateDtoIn phoneDtoIn) {
        Employee employee = employeeReadService.getById(phoneDtoIn.getEmployeeId());
        return phoneMapper.from(phoneDtoIn, employee);
    }

    private void fromUpdateDtoIn(Phone phone, PhoneUpdateDtoIn phoneDtoIn) {
        Employee employee = employeeReadService.getById(phoneDtoIn.getEmployeeId());
        phoneMapper.enrichWith(phone, phoneDtoIn, employee);
    }

}

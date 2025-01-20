package com.example.employeeservice.service.phone;

import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneUpdateDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;

import java.util.List;
import java.util.Set;

public interface PhoneService {

    PhoneDtoOut getPhoneDtoById(Long id);

    List<PhoneDtoOut> getPhonesByEmployeeId(Long employeeId);

    void validatePhonesCreate(List<String> phones);

    PhoneDtoOut toDto(Phone phone);

    PhoneDtoOut createPhone(PhoneCreateDtoIn phoneDtoIn);

    List<Phone> savePhones(Set<PhoneDtoIn> phones, Employee employee);

    PhoneDtoOut updatePhone(PhoneUpdateDtoIn phoneDtoIn);

    void deletePhone(Long id);

    void deletePhones(Set<Phone> phones);

}

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

    PhoneDtoOut getDtoById(Long id);

    List<PhoneDtoOut> getListByEmployeeId(Long employeeId);

    void validatePhonesCreate(List<String> phones);

    PhoneDtoOut toDto(Phone phone);

    PhoneDtoOut create(PhoneCreateDtoIn phoneDtoIn);

    List<Phone> saveList(Set<PhoneDtoIn> phones, Employee employee);

    PhoneDtoOut update(PhoneUpdateDtoIn phoneDtoIn);

    void deleteById(Long id);

    void deletePhones(Set<Phone> phones);

}

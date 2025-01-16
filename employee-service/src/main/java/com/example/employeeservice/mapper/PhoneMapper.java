package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.phone.PhoneDtoIn;
import com.example.employeeservice.dto.phone.PhoneDtoOut;
import com.example.employeeservice.dto.phone.PhoneFullDtoIn;
import com.example.employeeservice.dto.phone.PhoneFullDtoOut;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface PhoneMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    Phone fromDtoAndEmployee(PhoneDtoIn phoneDtoIn, Employee employee);

    Phone fromFullDto(PhoneFullDtoIn dto);

    @Mapping(target = "employeeId", source = "phone.employee.id")
    PhoneDtoOut toDto(Phone phone);

    @Mapping(target = "employee", source = "phone.employee")
    PhoneFullDtoOut toFullDto(Phone phone);

    void updateFromDto(PhoneDtoIn dtoIn, @MappingTarget Phone phone);
}

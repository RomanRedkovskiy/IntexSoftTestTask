package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneUpdateDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.model.entity.Employee;
import com.example.employeeservice.model.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PhoneMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    Phone fromDtoAndEmployee(PhoneDtoIn phoneDtoIn, Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    Phone fromDto(PhoneDtoIn phoneDtoIn);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    Phone fromCreateDtoAndEmployee(PhoneCreateDtoIn dto, Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    void fromUpdateDtoAndEmployee(@MappingTarget Phone phone, PhoneUpdateDtoIn dto, Employee employee);

    @Mapping(target = "employeeId", source = "phone.employee.id")
    PhoneDtoOut toDto(Phone phone);

}

package com.example.employeeservice.controller;

import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneUpdateDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.service.phone.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
@Validated
@Tag(name = "Phone Controller", description = "Controller for managing employee phones")
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping("{id}")
    @Operation(summary = "Retrieve a phone by ID", description = "Fetch details of a phone using its unique identifier.")
    public PhoneDtoOut getPhoneById(@PathVariable Long id) {
        return phoneService.getPhoneDtoById(id);
    }

    @GetMapping("employee/{employeeId}")
    @Operation(summary = "Retrieve a phone list by employee ID", description = "Fetch a list of phones using employee unique identifier.")
    public List<PhoneDtoOut> getPhonesByEmployeeId(@PathVariable Long employeeId) {
        return phoneService.getPhonesByEmployeeId(employeeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new phone", description = "Add a new employee's phone to the system.")
    public PhoneDtoOut addPhone(@RequestBody @Valid PhoneCreateDtoIn phoneDtoIn) {
        return phoneService.createPhone(phoneDtoIn);
    }

    @PutMapping
    @Operation(summary = "Update an existing phone", description = "Edit details an existing phone.")
    public PhoneDtoOut updatePhone(@RequestBody @Valid PhoneUpdateDtoIn phoneDtoIn) {
        return phoneService.updatePhone(phoneDtoIn);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a phone", description = "Remove a phone from the system using its ID.")
    public void deletePhone(@PathVariable Long id) {
        phoneService.deletePhone(id);
    }
}

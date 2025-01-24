package com.example.employeeservice.integration;

import com.example.employeeservice.TestcontainersConfiguration;
import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.model.enums.EmployeeRole;
import com.example.employeeservice.service.employee.EmployeeService;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWireMock(port = 8080)
public class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @BeforeAll
    void setUp() {
        WireMock.stubFor(WireMock.get("/departments/1")
                .willReturn(WireMock.aResponse()
                        .withBody("{}")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        Set<PhoneDtoIn> phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375446086900"));
        phones.add(new PhoneDtoIn("+375447082800"));
        EmployeeCreateDtoIn employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Anastasia")
                .surname("Red")
                .salary(2000L)
                .role(EmployeeRole.MANAGER)
                .managerId(null)
                .departmentId(1L)
                .phones(phones)
                .build();
        employeeService.create(employeeCreateDtoIn);

        phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375446186900"));
        employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Johny")
                .surname("Doe")
                .salary(1000L)
                .role(EmployeeRole.DEFAULT)
                .managerId(1L)
                .departmentId(1L)
                .phones(phones)
                .build();
        employeeService.create(employeeCreateDtoIn);

        phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375445786345"));
        phones.add(new PhoneDtoIn("+375447182800"));
        employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Andrey")
                .surname("Pszedkowski")
                .salary(1000L)
                .role(EmployeeRole.HEAD)
                .managerId(null)
                .departmentId(1L)
                .phones(phones)
                .build();
        employeeService.create(employeeCreateDtoIn);
    }

    @Test
    void findAllEmployees_WhenEmployeesExist_ThenReturnsEmployeesList() throws Exception {
        mockMvc.perform(get("/employees/page")).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.content[0].role").isNotEmpty(),
                jsonPath("$.content[1].name").isNotEmpty(),
                jsonPath("$.content[2].phones").isNotEmpty()
        );
    }

    @Test
    void findEmployeeById_WhenEmployeeExists_ThenReturnsEmployee() throws Exception {
        Set<PhoneDtoIn> phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375449986978"));
        EmployeeCreateDtoIn employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Johnny")
                .surname("Sins")
                .salary(100L)
                .role(EmployeeRole.DEFAULT)
                .managerId(null)
                .departmentId(1L)
                .phones(phones)
                .build();
        EmployeeDtoOut employeeDtoOut = employeeService.create(employeeCreateDtoIn);
        mockMvc.perform(get("/employees/" + employeeDtoOut.getId())).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.id").value(employeeDtoOut.getId()),
                jsonPath("$.name").value("Johnny"),
                jsonPath("$.surname").value("Sins"),
                jsonPath("$.phones[0].phone").value("+375449986978"),
                jsonPath("$.role").value("DEFAULT")
        );
    }

    @Test
    void findEmployeeById_WhenEmployeeNotExists_ThenThrowsNotFoundException() throws Exception {
        mockMvc.perform(get("/employees/22612")).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createEmployee_WhenValidDataProvided_ThenReturnsCreatedEmployee() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Name5",
                                    "surname": "Surname5",
                                    "salary": 4000,
                                    "role": "DEFAULT",
                                    "managerId": 1,
                                    "departmentId": 1,
                                    "phones": [
                                        {
                                            "phone": "+375445550011"
                                        }
                                    ]
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Name5"),
                        jsonPath("$.role").value("DEFAULT"),
                        jsonPath("$.managerId").value(1),
                        jsonPath("$.departmentId").value(1),
                        jsonPath("$.phones[0].phone").value("+375445550011")
                );
    }

    @Test
    void createEmployee_WhenManagerIdIsInvalidThrowsBadRequestException() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Name5",
                                    "surname": "Surname5",
                                    "salary": 4000,
                                    "role": "DEFAULT",
                                    "managerId": 2,
                                    "departmentId": 1,
                                    "phones": [
                                        {
                                            "phone": "+375446661100"
                                        }
                                    ]
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void createEmployee_WhenDuplicatedPhoneThrowsConflictException() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Name5",
                                    "surname": "Surname5",
                                    "salary": 4000,
                                    "role": "DEFAULT",
                                    "managerId": 2,
                                    "departmentId": 1,
                                    "phones": [
                                        {
                                            "phone": "+375446661100"
                                        }
                                    ]
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void updateEmployee_WhenValidDataProvided_ThenReturnsUpdatedEmployee() throws Exception {
        mockMvc.perform(put("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 2,
                                    "name": "Johny_Updated",
                                    "surname": "Doe_Updated",
                                    "salary": 1002,
                                    "role": "DEFAULT",
                                    "managerId": 1,
                                    "departmentId": 1
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Johny_Updated"),
                        jsonPath("$.surname").value("Doe_Updated"),
                        jsonPath("$.salary").value(1002),
                        jsonPath("$.role").value("DEFAULT"),
                        jsonPath("$.managerId").value(1)
                );
    }

    @Test
    void updateEmployee_WhenManagerIsInvalid_ThenThrowsBadRequestException() throws Exception {
        mockMvc.perform(put("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 2,
                                    "name": "Johny_Updated",
                                    "surname": "Doe_Updated",
                                    "salary": 1002,
                                    "role": "DEFAULT",
                                    "managerId": 2,
                                    "departmentId": 1
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteEmployee_WhenEmployeeIdIsValid_ThenDeletesEmployee() throws Exception {
        Set<PhoneDtoIn> phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375449986900"));
        phones.add(new PhoneDtoIn("+375449982800"));
        EmployeeCreateDtoIn employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Name10")
                .surname("Surname10")
                .salary(200L)
                .role(EmployeeRole.DEFAULT)
                .managerId(null)
                .departmentId(1L)
                .phones(phones)
                .build();
        EmployeeDtoOut employeeDtoOut = employeeService.create(employeeCreateDtoIn);
        mockMvc.perform(get("/employees/" + employeeDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        mockMvc.perform(delete("/employees/" + employeeDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk()
                );
        mockMvc.perform(get("/employees/" + employeeDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteEmployee_WhenDeleteManager_ThenNullManagerIdForSubordinates() throws Exception {
        EmployeeCreateDtoIn employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("MANAGER_TEST_10")
                .surname("MANAGER_TEST_10")
                .salary(2000L)
                .role(EmployeeRole.MANAGER)
                .managerId(null)
                .departmentId(1L)
                .build();
        EmployeeDtoOut managerDtoOut = employeeService.create(employeeCreateDtoIn);

        employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("DEFAULT_TEST_10")
                .surname("DEFAULT_TEST_10")
                .salary(1000L)
                .role(EmployeeRole.DEFAULT)
                .managerId(managerDtoOut.getId())
                .departmentId(1L)
                .build();
        EmployeeDtoOut employeeDtoOut = employeeService.create(employeeCreateDtoIn);

        mockMvc.perform(get("/employees/" + employeeDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.managerId").value(managerDtoOut.getId())
                );

        mockMvc.perform(delete("/employees/" + managerDtoOut.getId()))
                .andExpectAll(
                        status().isOk()
                );

        mockMvc.perform(get("/employees/" + managerDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );

        mockMvc.perform(get("/employees/" + employeeDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.managerId").isEmpty()
                );
    }

}

package com.example.employeeservice.integration;

import com.example.employeeservice.TestcontainersConfiguration;
import com.example.employeeservice.dto.employee.in.EmployeeCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneCreateDtoIn;
import com.example.employeeservice.dto.phone.in.PhoneDtoIn;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import com.example.employeeservice.kafka.NotificationProducer;
import com.example.employeeservice.model.enums.EmployeeRole;
import com.example.employeeservice.service.employee.EmployeeService;
import com.example.employeeservice.service.phone.PhoneService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
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
@AutoConfigureWireMock(port = 8585)
public class PhoneControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PhoneService phoneService;

    @BeforeAll
    void setUp() {
        NotificationProducer notificationProducer = mock(NotificationProducer.class);
        ReflectionTestUtils.setField(phoneService, "notificationProducer", notificationProducer);
        ReflectionTestUtils.setField(employeeService, "notificationProducer", notificationProducer);

        WireMock.stubFor(WireMock.get("/api/v1/departments/1")
                .willReturn(WireMock.aResponse()
                        .withBody("{}")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        Set<PhoneDtoIn> phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375446996900"));
        phones.add(new PhoneDtoIn("+375447992800"));
        EmployeeCreateDtoIn employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Nikita")
                .surname("Green")
                .salary(1000L)
                .role(EmployeeRole.MANAGER)
                .managerId(null)
                .departmentId(1L)
                .phones(phones)
                .build();
        employeeService.create(employeeCreateDtoIn);

        phones = new HashSet<>();
        phones.add(new PhoneDtoIn("+375446186911"));
        employeeCreateDtoIn = EmployeeCreateDtoIn.builder()
                .name("Olga")
                .surname("Yellow")
                .salary(200L)
                .role(EmployeeRole.DEFAULT)
                .managerId(1L)
                .departmentId(1L)
                .phones(phones)
                .build();
        employeeService.create(employeeCreateDtoIn);
    }

    @Test
    void getPhoneById_WhenPhoneExists_ThenReturnsEmployeeDetails() throws Exception {
        String phone = "+375441112233";
        Long employeeId = 1L;
        PhoneCreateDtoIn phoneCreateDtoIn = PhoneCreateDtoIn.builder()
                .phone(phone)
                .employeeId(employeeId)
                .build();
        PhoneDtoOut phoneDtoOut = phoneService.create(phoneCreateDtoIn);
        mockMvc.perform(get("/phones/" + phoneDtoOut.getId())).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.phone").value(phone)
        );
    }

    @Test
    void getPhoneById_WhenPhoneNotExists_ThenThrowsNotFoundException() throws Exception {
        mockMvc.perform(get("/phones/1234")).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void getPhonesByEmployeeId_WhenEmployeeExists_ThenReturnsEmployeePhones() throws Exception {
        String phone = "+375442223344";
        Long employeeId = 1L;
        PhoneCreateDtoIn phoneCreateDtoIn = PhoneCreateDtoIn.builder()
                .phone(phone)
                .employeeId(employeeId)
                .build();
        phoneService.create(phoneCreateDtoIn);
        mockMvc.perform(get("/phones/employee/" + employeeId)).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void getPhonesByEmployeeId_WhenEmployeeNotExists_ThenThrowsNotFoundException() throws Exception {
        mockMvc.perform(get("/phones/employee/1569")).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createPhone_WhenValidDataProvided_ThenReturnsCreatedPhone() throws Exception {
        mockMvc.perform(post("/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "phone": "+375445781234",
                                    "employeeId": 2
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.phone").value("+375445781234")
                );
    }

    @Test
    void createPhone_WhenInValidEmployeeIdProvided_ThenThrowsNotFoundException() throws Exception {
        mockMvc.perform(post("/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "phone": "+375445781230",
                                    "employeeId": 1231231
                                }
                                """))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void updatePhone_WhenValidDataProvided_ThenReturnsUpdatedPhone() throws Exception {
        String phone = "+375447891923";
        Long employeeId = 1L;
        PhoneCreateDtoIn phoneCreateDtoIn = PhoneCreateDtoIn.builder()
                .phone(phone)
                .employeeId(employeeId)
                .build();
        PhoneDtoOut phoneDtoOut = phoneService.create(phoneCreateDtoIn);
        mockMvc.perform(put("/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": %d,
                                    "phone": "+375447991923",
                                    "employeeId": 1
                                }
                                """.formatted(phoneDtoOut.getId())))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.phone").value("+375447991923")
                );
    }

    @Test
    void deletePhone_WhenValidPhoneIdProvided_ThenDeletesPhone() throws Exception {
        String phone = "+375447891984";
        Long employeeId = 1L;
        PhoneCreateDtoIn phoneCreateDtoIn = PhoneCreateDtoIn.builder()
                .phone(phone)
                .employeeId(employeeId)
                .build();
        PhoneDtoOut phoneDtoOut = phoneService.create(phoneCreateDtoIn);
        mockMvc.perform(get("/phones/" + phoneDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk()
                );
        mockMvc.perform(delete("/phones/" + phoneDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk()
                );
        mockMvc.perform(get("/phones/" + phoneDtoOut.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound()
                );
    }

}

package com.example.departmentservice.integration;

import com.example.departmentservice.TestcontainersConfiguration;
import com.example.departmentservice.dto.department.in.DepartmentCreateDtoIn;
import com.example.departmentservice.dto.department.out.DepartmentDtoOut;
import com.example.departmentservice.kafka.NotificationProducer;
import com.example.departmentservice.service.department.DepartmentService;
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
public class DepartmentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DepartmentService departmentService;

    @BeforeAll
    void setUp() {
        NotificationProducer notificationProducer = mock(NotificationProducer.class);
        ReflectionTestUtils.setField(departmentService, "notificationProducer", notificationProducer);

        DepartmentCreateDtoIn departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name("Department 1")
                .location("Minsk")
                .build();
        departmentService.create(departmentCreateDtoIn);

        departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name("Department 2")
                .location("Brest")
                .build();
        departmentService.create(departmentCreateDtoIn);

        departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name("Department 3")
                .location("Warsaw")
                .build();
        departmentService.create(departmentCreateDtoIn);
    }

    @Test
    void findAllDepartments_WhenDepartmentsExist_ThenReturnsDepartmentsList() throws Exception {
        mockMvc.perform(get("/departments")).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.[0].name").isNotEmpty(),
                jsonPath("$.[1].location").isNotEmpty(),
                jsonPath("$.[2].id").isNotEmpty()
        );
    }

    @Test
    void findDepartmentById_WhenDepartmentExists_ThenReturnsDepartment() throws Exception {
        String name = "Department 4";
        String location = "Berlin";
        DepartmentCreateDtoIn departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name(name)
                .location(location)
                .build();
        DepartmentDtoOut departmentDtoOut = departmentService.create(departmentCreateDtoIn);

        WireMock.stubFor(WireMock.get("/api/v1/employees/department/" + departmentDtoOut.getId())
                .willReturn(WireMock.aResponse()
                        .withBody("[]")
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        mockMvc.perform(get("/departments/" + departmentDtoOut.getId())).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.name").value(name),
                jsonPath("$.location").value(location),
                jsonPath("$.id").value(departmentDtoOut.getId())
        );
    }

    @Test
    void findDepartmentById_WhenDepartmentNotExists_ThenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/departments/12312")).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void createDepartment_WhenValidDataProvided_ThenReturnsCreatedDepartment() throws Exception {
        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Department 10",
                                    "location": "Brest"
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value("Department 10"),
                        jsonPath("$.location").value("Brest")
                );
    }

    @Test
    void createDepartment_WhenInvalidDataProvided_ThenThrowsBadRequestException() throws Exception {
        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "location": "Brest"
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void updateDepartment_WhenDepartmentExists_ThenReturnsDepartment() throws Exception {
        String initialName = "Department 548";
        String finalName = "Department 45";
        String location = "Paris";
        DepartmentCreateDtoIn departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name(initialName)
                .location(location)
                .build();
        DepartmentDtoOut departmentDtoOut = departmentService.create(departmentCreateDtoIn);

        mockMvc.perform(put("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": %d,
                                    "name": "%s",
                                    "location": "Paris"
                                }
                                """.formatted(departmentDtoOut.getId(), finalName)))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.name").value(finalName),
                        jsonPath("$.location").value(location),
                        jsonPath("$.id").value(departmentDtoOut.getId())
                );
    }

    @Test
    void updateDepartment_WhenDepartmentIdNotExists_ThenThrowsNotFoundException() throws Exception {
        mockMvc.perform(put("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 123123,
                                    "name": "Department name",
                                    "location": "Paris"
                                }
                                """))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteDepartmentById_WhenDepartmentExists_ThenDeletesDepartment() throws Exception {
        String name = "Department Delete";
        String location = "Unknown";
        DepartmentCreateDtoIn departmentCreateDtoIn = DepartmentCreateDtoIn.builder()
                .name(name)
                .location(location)
                .build();
        DepartmentDtoOut departmentDtoOut = departmentService.create(departmentCreateDtoIn);

        WireMock.stubFor(WireMock.delete("/api/v1/employees/department/" + departmentDtoOut.getId())
                .willReturn(WireMock.aResponse()
                        .withStatus(200)));

        mockMvc.perform(get("/departments/" + departmentDtoOut.getId())).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.name").value(name),
                jsonPath("$.location").value(location),
                jsonPath("$.id").value(departmentDtoOut.getId())
        );

        mockMvc.perform(delete("/departments/" + departmentDtoOut.getId())).andExpectAll(
                status().isOk()
        );

        mockMvc.perform(get("/departments/" + departmentDtoOut.getId())).andExpectAll(
                status().isNotFound(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
    }

}

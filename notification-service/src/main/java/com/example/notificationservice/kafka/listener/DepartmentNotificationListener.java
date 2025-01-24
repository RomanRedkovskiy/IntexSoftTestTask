package com.example.notificationservice.kafka.listener;

import com.example.notificationservice.dto.department.DepartmentDtoOut;
import com.example.notificationservice.kafka.deserialization.NotificationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DepartmentNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(DepartmentNotificationListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(groupId = "department-group", topics = "department-create-topic")
    public void logDepartmentCreate(NotificationData notificationData) throws IOException {
        DepartmentDtoOut departmentDtoOut = objectMapper.readValue(notificationData.getData(), DepartmentDtoOut.class);
        log.info("Department created: {}", departmentDtoOut.toString());
    }

    @KafkaListener(groupId = "department-group", topics = "department-update-topic")
    public void logDepartmentUpdate(NotificationData notificationData) throws IOException {
        DepartmentDtoOut departmentDtoOut = objectMapper.readValue(notificationData.getData(), DepartmentDtoOut.class);
        log.info("Department updated: {}", departmentDtoOut.toString());
    }

    @KafkaListener(groupId = "department-group", topics = "department-delete-topic")
    public void logDepartmentDelete(NotificationData notificationData) throws IOException {
        Long id = objectMapper.readValue(notificationData.getData(), Long.class);
        log.info("Department deleted: {}", id.toString());
    }

}

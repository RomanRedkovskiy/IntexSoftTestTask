package com.example.notificationservice.kafka.listener;

import com.example.notificationservice.dto.employee.EmployeeDtoOut;
import com.example.notificationservice.kafka.deserialization.NotificationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class EmployeeNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EmployeeNotificationListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(groupId = "employee-group", topics = "employee-create-topic")
    public void logEmployeeCreate(NotificationData notificationData) throws IOException {
        EmployeeDtoOut employeeDtoOut = objectMapper.readValue(notificationData.getData(), EmployeeDtoOut.class);
        log.info("Employee created: {}", employeeDtoOut.toString());
    }

    @KafkaListener(groupId = "employee-group", topics = "employee-update-topic")
    public void logEmployeeUpdate(NotificationData notificationData) throws IOException {
        EmployeeDtoOut employeeDtoOut = objectMapper.readValue(notificationData.getData(), EmployeeDtoOut.class);
        log.info("Employee updated: {}", employeeDtoOut.toString());
    }

    @KafkaListener(groupId = "employee-group", topics = "employee-delete-topic")
    public void logEmployeeDelete(NotificationData notificationData) throws IOException {
        Long id = objectMapper.readValue(notificationData.getData(), Long.class);
        log.info("Employee deleted: {}", id.toString());
    }
}

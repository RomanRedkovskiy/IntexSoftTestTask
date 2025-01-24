package com.example.notificationservice.kafka.listener;

import com.example.notificationservice.dto.phone.PhoneDtoOut;
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
public class PhoneNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(PhoneNotificationListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(groupId = "phone-group", topics = "phone-create-topic")
    public void logPhoneCreate(NotificationData notificationData) throws IOException {
        PhoneDtoOut phoneDtoOut = objectMapper.readValue(notificationData.getData(), PhoneDtoOut.class);
        log.info("Phone created: {}", phoneDtoOut.toString());
    }

    @KafkaListener(groupId = "phone-group", topics = "phone-update-topic")
    public void logPhoneUpdate(NotificationData notificationData) throws IOException {
        PhoneDtoOut phoneDtoOut = objectMapper.readValue(notificationData.getData(), PhoneDtoOut.class);
        log.info("Phone updated: {}", phoneDtoOut.toString());
    }

    @KafkaListener(groupId = "phone-group", topics = "phone-delete-topic")
    public void logPhoneDelete(NotificationData notificationData) throws IOException {
        Long id = objectMapper.readValue(notificationData.getData(), Long.class);
        log.info("Phone deleted: {}", id.toString());
    }

}

package com.example.notificationservice.kafka.deserialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InitialDeserializer implements Deserializer<NotificationData> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration logic, if needed
    }

    @Override
    public NotificationData deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("adasd");
        }
        try {
            return new NotificationData(data);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing object", e);
        }
    }

    @Override
    public void close() {
        // Cleanup logic, if needed
    }
}
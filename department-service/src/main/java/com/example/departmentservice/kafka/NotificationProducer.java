package com.example.departmentservice.kafka;

import com.example.departmentservice.config.KafkaTopicConfig;
import com.example.departmentservice.dto.department.out.DepartmentDtoOut;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTopicConfig kafkaTopicConfig;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreate(DepartmentDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.departmentCreateTopic, dto);
    }

    public void sendUpdate(DepartmentDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.departmentUpdateTopic, dto);
    }

    public void sendDelete(Long id) {
        kafkaTemplate.send(kafkaTopicConfig.departmentDeleteTopic, id);
    }

}

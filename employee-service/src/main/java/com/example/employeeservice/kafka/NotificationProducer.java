package com.example.employeeservice.kafka;

import com.example.employeeservice.config.KafkaTopicConfig;
import com.example.employeeservice.dto.employee.out.EmployeeDtoOut;
import com.example.employeeservice.dto.phone.out.PhoneDtoOut;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTopicConfig kafkaTopicConfig;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmployeeCreate(EmployeeDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.employeeCreateTopic, dto);
    }

    public void sendEmployeeUpdate(EmployeeDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.employeeUpdateTopic, dto);
    }

    public void sendPhoneCreate(PhoneDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.phoneCreateTopic, dto);
    }

    public void sendPhoneUpdate(PhoneDtoOut dto) {
        kafkaTemplate.send(kafkaTopicConfig.phoneUpdateTopic, dto);
    }

    public void sendPhoneDelete(Long id) {
        kafkaTemplate.send(kafkaTopicConfig.phoneDeleteTopic, id);
    }


    public void sendEmployeeDelete(Long id) {
        kafkaTemplate.send(kafkaTopicConfig.employeeDeleteTopic, id);
    }

}

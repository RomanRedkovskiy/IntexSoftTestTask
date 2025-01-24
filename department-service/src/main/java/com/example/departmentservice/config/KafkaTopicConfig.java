package com.example.departmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.departmentCreate}")
    public String departmentCreateTopic;

    @Value("${kafka.topics.departmentUpdate}")
    public String departmentUpdateTopic;

    @Value("${kafka.topics.departmentDelete}")
    public String departmentDeleteTopic;
}

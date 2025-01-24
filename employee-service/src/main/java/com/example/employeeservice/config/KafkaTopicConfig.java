package com.example.employeeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.employeeCreate}")
    public String employeeCreateTopic;

    @Value("${kafka.topics.employeeUpdate}")
    public String employeeUpdateTopic;

    @Value("${kafka.topics.employeeDelete}")
    public String employeeDeleteTopic;

    @Value("${kafka.topics.phoneCreate}")
    public String phoneCreateTopic;

    @Value("${kafka.topics.phoneUpdate}")
    public String phoneUpdateTopic;

    @Value("${kafka.topics.phoneDelete}")
    public String phoneDeleteTopic;
}

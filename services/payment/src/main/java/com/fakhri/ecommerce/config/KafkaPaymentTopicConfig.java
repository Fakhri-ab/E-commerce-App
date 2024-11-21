package com.fakhri.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaPaymentTopicConfig {

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder
                .name("payment-topic")
                .build();
    }
}

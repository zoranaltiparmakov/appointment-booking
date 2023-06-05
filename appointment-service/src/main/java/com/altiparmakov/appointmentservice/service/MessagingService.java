package com.altiparmakov.appointmentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagingService {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String topicName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(topicName,
                                      "appointment." + routingKey,
                                      message);
    }
}

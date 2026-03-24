package com.oep.orderingestor.infraestructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.oep.orderingestor.domain.event.OrderCreatedEvent;
import com.oep.orderingestor.domain.port.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {
    
    private static final String TOPIC = "order.created";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void publish(OrderCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.orderId().toString(), payload)
                .whenComplete((result, ex) -> {
                    if(ex != null)
                        log.error("Error publishing event orderId={}", event.orderId(), ex);
                    else
                        log.info("Event published orderId={} partition={} offset={}", event.orderId(), result.getRecordMetadata().partition(),  result.getRecordMetadata().offset());
                });
        }catch(Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }


}

package com.oep.orderprocessor.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.oep.orderprocessor.application.AlreadyProcessedException;
import com.oep.orderprocessor.application.ProcessOrderUseCase;
import com.oep.orderprocessor.domain.event.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateConsumer {
    
    private final ProcessOrderUseCase proccessOrderUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics= "order.created", groupId= "order-processor", containerFactory= "kafkaListenerContainerFactory")
    public void consume(@Payload String message,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment ack) 
    {
        log.info("Received event partition={} offset={}", partition, offset);

        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            proccessOrderUseCase.execute(event);
            ack.acknowledge();
        }catch(AlreadyProcessedException e) {
            log.warn("Duplicate event ignored: {}", e.getMessage());
            ack.acknowledge();
        }catch(Exception e) {
            log.error("Error processing message partition={} offset={}", partition, offset, e);
        }
    }
}

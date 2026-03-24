package com.oep.orderingestor.application;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.oep.orderingestor.domain.event.OrderCreatedEvent;
import com.oep.orderingestor.domain.model.Order;
import com.oep.orderingestor.domain.port.EventPublisher;
import com.oep.orderingestor.domain.port.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
    
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public Order execute(Order order) {
        Order saved = orderRepository.save(Order.builder()
            .id(UUID.randomUUID())
            .customerId(order.getCustomerId())
            .totalAmount(order.getTotalAmount())
            .status("CREATED")
            .createdAt(Instant.now())
            .build()
        );

        eventPublisher.publish(new OrderCreatedEvent(
            saved.getId(),
            saved.getCustomerId(),
            saved.getTotalAmount(),
            saved.getCreatedAt())
        );

        return saved;
    } 
}

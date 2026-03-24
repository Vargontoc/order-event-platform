package com.oep.orderingestor.infraestructure.persistence;

import org.springframework.stereotype.Component;

import com.oep.orderingestor.domain.model.Order;
import com.oep.orderingestor.domain.port.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    
    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderEntity.builder()
            .customerId(order.getCustomerId())
            .totalAmount(order.getTotalAmount())
            .status(order.getStatus())
            .createdAt(order.getCreatedAt())
            .build();
        
        OrderEntity saved = jpaRepository.save(entity);

        return Order.builder()
            .id(saved.getId())
            .customerId(saved.getCustomerId())
            .totalAmount(saved.getTotalAmount())
            .status(saved.getStatus())
            .createdAt(saved.getCreatedAt())
            .build();
    }
    
}

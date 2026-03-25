package com.oep.orderprocessor.infraestructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.oep.orderprocessor.domain.model.OrderProccess;
import com.oep.orderprocessor.domain.port.OrderProcessRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderProcessRepositoryAdapter implements  OrderProcessRepository {
    
    private final OrderProcessJpaRepository jpaRepository;

    @Override
    public OrderProccess save(OrderProccess process) {
        OrderProcessEntity entity = OrderProcessEntity.builder()
            .orderId(process.getOrderId())
            .customerId(process.getCustomerId())
            .totalAmount(process.getTotalAmount())
            .status(process.getStatus())
            .failureReason(process.getFailureReason())
            .receivedAt(process.getReceivedAt())
            .processedAt(process.getProcessedAt())
            .build();

        OrderProcessEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
        
    }
    @Override
    public Optional<OrderProccess> findByOrderId(UUID orderId) {
        return jpaRepository.findByOrderId(orderId).map(this::toDomain);
    }

    private OrderProccess toDomain(OrderProcessEntity e) {
        return OrderProccess.builder()
            .id(e.getId())
            .customerId(e.getCustomerId())
            .failureReason(e.getFailureReason())
            .orderId(e.getOrderId())
            .processedAt(e.getProcessedAt())
            .receivedAt(e.getReceivedAt())
            .status(e.getStatus())
            .totalAmount(e.getTotalAmount())
            .build();

    }
    
}

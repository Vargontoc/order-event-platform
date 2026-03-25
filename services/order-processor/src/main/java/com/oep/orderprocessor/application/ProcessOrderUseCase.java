package com.oep.orderprocessor.application;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oep.orderprocessor.domain.event.OrderCreatedEvent;
import com.oep.orderprocessor.domain.model.OrderProccess;
import com.oep.orderprocessor.domain.model.ProcessStatus;
import com.oep.orderprocessor.domain.port.OrderProcessRepository;
import com.oep.orderprocessor.domain.port.StockReserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessOrderUseCase {
    private final OrderProcessRepository repository;
    private final StockReserver stockReserver;

    @Transactional
    public void execute(OrderCreatedEvent event) {

        // Indempotencia
        repository.findByOrderId(event.orderId()).ifPresent(existing -> {
            log.warn("Event already processed orderId={} status={}", event.orderId(), existing.getStatus());
            throw new AlreadyProcessedException(event.orderId());
        });

        // Registramos evento recibido
        OrderProccess process = repository.save(OrderProccess.builder()
            .orderId(event.orderId())
            .customerId(event.customerId())
            .totalAmount(event.totalAmount())
            .status(ProcessStatus.RECEIVED)
            .receivedAt(Instant.now())
            .build());

            try {
                boolean reserved = stockReserver.reserve(event.orderId(), event.totalAmount());
                // Reservamos stock
                if (!reserved) {
                    repository.save(process.toBuilder()
                        .status(ProcessStatus.FAILED)
                        .failureReason("Stock not available")
                        .processedAt(Instant.now())
                        .build());
                    return;
                }

                repository.save(process.toBuilder()
                    .status(ProcessStatus.COMPLETED)
                    .processedAt(Instant.now()).build());
                log.info("Order processed succesfully orderId={}", event.orderId());
        }catch(Exception e) {
            log.error("Error processing order orderId={}", event.orderId(), e);
            repository.save(process.toBuilder()
                .status(ProcessStatus.FAILED)
                .failureReason(e.getMessage())
                .processedAt(Instant.now())
                .build());
            throw e;
        }
    }
}

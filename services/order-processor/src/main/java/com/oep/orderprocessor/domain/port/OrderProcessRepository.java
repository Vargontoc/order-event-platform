package com.oep.orderprocessor.domain.port;

import java.util.Optional;
import java.util.UUID;

import com.oep.orderprocessor.domain.model.OrderProccess;

public interface OrderProcessRepository {
    OrderProccess save(OrderProccess process);
    Optional<OrderProccess> findByOrderId(UUID orderId);
}

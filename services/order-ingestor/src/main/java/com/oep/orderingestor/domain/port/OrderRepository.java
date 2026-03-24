package com.oep.orderingestor.domain.port;

import com.oep.orderingestor.domain.model.Order;

public interface OrderRepository {
    Order save(Order order);
}

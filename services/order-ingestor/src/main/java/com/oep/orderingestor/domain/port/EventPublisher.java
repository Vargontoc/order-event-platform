package com.oep.orderingestor.domain.port;

import com.oep.orderingestor.domain.event.OrderCreatedEvent;

public interface  EventPublisher {
    void publish(OrderCreatedEvent event);
}

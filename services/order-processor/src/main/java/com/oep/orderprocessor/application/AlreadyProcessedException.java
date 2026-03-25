package com.oep.orderprocessor.application;

import java.util.UUID;

public class AlreadyProcessedException extends RuntimeException {
    public AlreadyProcessedException(UUID orderId) {
        super("Order already processed: " + orderId);
    }
}

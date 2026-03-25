package com.oep.orderprocessor.domain.port;

import java.math.BigDecimal;
import java.util.UUID;

public interface StockReserver {
    boolean reserve(UUID orderId, BigDecimal amount);
}

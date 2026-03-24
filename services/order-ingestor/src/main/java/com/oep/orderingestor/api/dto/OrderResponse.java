package com.oep.orderingestor.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


public record OrderResponse(UUID id, String customerId, BigDecimal totalAmount, String status, Instant createAt) {
    
}

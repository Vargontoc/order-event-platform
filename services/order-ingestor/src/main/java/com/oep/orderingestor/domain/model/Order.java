package com.oep.orderingestor.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Order {
    UUID id;
    String customerId;
    BigDecimal totalAmount;
    String status;
    Instant createdAt;
}

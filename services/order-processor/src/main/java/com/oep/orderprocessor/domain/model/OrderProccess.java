package com.oep.orderprocessor.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder= true)
public class OrderProccess {
    UUID id;
    UUID orderId;
    String customerId;
    BigDecimal totalAmount;
    ProcessStatus status;
    String failureReason;
    Instant receivedAt;
    Instant processedAt
;}

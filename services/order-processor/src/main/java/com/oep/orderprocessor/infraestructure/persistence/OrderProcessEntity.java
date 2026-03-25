package com.oep.orderprocessor.infraestructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.oep.orderprocessor.domain.model.ProcessStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_process",
       indexes = @Index(name = "idx_order_id", columnList = "orderId", unique = true))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID orderId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessStatus status;

    private String failureReason;

    @Column(nullable = false)
    private Instant receivedAt;

    private Instant processedAt;
}

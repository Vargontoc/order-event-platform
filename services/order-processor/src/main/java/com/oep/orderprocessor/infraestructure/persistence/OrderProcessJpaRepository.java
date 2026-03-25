package com.oep.orderprocessor.infraestructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface  OrderProcessJpaRepository extends JpaRepository<OrderProcessEntity, UUID> {
    Optional<OrderProcessEntity>  findByOrderId(UUID orderId);
}

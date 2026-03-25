package com.oep.orderprocessor.infraestructure.stock;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.oep.orderprocessor.domain.port.StockReserver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StockReserverStub implements StockReserver {@Override
    public boolean reserve(UUID orderId, BigDecimal amount) {
        log.info("[STUB] Stock reserved orderId={} amount={}", orderId, amount);
        return true;
    }
    
}

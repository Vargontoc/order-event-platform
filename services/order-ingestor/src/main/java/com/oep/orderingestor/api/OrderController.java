package com.oep.orderingestor.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oep.orderingestor.api.dto.CreateOrderRequest;
import com.oep.orderingestor.api.dto.OrderResponse;
import com.oep.orderingestor.application.CreateOrderUseCase;
import com.oep.orderingestor.domain.model.Order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse postMethodName(@RequestBody @Valid CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(Order.builder().customerId(request.customerId()).totalAmount(request.totalAmount()).build());
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getTotalAmount(), order.getStatus(), order.getCreatedAt());
    }
    
}

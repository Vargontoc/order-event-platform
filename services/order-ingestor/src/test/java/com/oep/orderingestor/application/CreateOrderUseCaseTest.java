
package com.oep.orderingestor.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oep.orderingestor.domain.event.OrderCreatedEvent;
import com.oep.orderingestor.domain.model.Order;
import com.oep.orderingestor.domain.port.EventPublisher;
import com.oep.orderingestor.domain.port.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {
    
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private CreateOrderUseCase useCase;


    @Test
    void should_save_order_and_publish_event() {
        // Arrange
        Order input = Order.builder()
                .customerId("customer-123")
                .totalAmount(new BigDecimal("99.99"))
                .build();

        Order saved = Order.builder()
                .id(UUID.randomUUID())
                .customerId("customer-123")
                .totalAmount(new BigDecimal("99.99"))
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        when(orderRepository.save(any())).thenReturn(saved);

        // Act
        Order result = useCase.execute(input);

        // Assert — el pedido se guardó
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo("CREATED");
        assertThat(result.getCustomerId()).isEqualTo("customer-123");

        // Assert — se publicó exactamente un evento con los datos correctos
        ArgumentCaptor<OrderCreatedEvent> captor =
                ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(eventPublisher, times(1)).publish(captor.capture());

        OrderCreatedEvent event = captor.getValue();
        assertThat(event.orderId()).isEqualTo(saved.getId());
        assertThat(event.customerId()).isEqualTo("customer-123");
        assertThat(event.totalAmount()).isEqualByComparingTo("99.99");
    }

    @Test
    void should_always_set_status_to_created() {
        // Arrange — aunque el input venga con otro status, el caso de uso lo ignora
        Order input = Order.builder()
                .customerId("customer-456")
                .totalAmount(new BigDecimal("50.00"))
                .status("WHATEVER")
                .build();

        when(orderRepository.save(any())).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            return Order.builder()
                    .id(UUID.randomUUID())
                    .customerId(o.getCustomerId())
                    .totalAmount(o.getTotalAmount())
                    .status(o.getStatus())
                    .createdAt(Instant.now())
                    .build();
        });

        // Act
        Order result = useCase.execute(input);

        // Assert
        assertThat(result.getStatus()).isEqualTo("CREATED");
    }
}

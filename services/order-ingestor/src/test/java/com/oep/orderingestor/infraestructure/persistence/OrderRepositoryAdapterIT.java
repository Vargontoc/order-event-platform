package com.oep.orderingestor.infraestructure.persistence;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.oep.orderingestor.domain.model.Order;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(OrderRepositoryAdapter.class)
public class OrderRepositoryAdapterIT {
        @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderRepositoryAdapter adapter;

    @Test
    void should_persist_order_and_return_with_generated_id() {
        // Arrange
        Order order = Order.builder()
                .customerId("customer-123")
                .totalAmount(new BigDecimal("149.99"))
                .status("CREATED")
                .build();

        // Act
        Order saved = adapter.save(order);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCustomerId()).isEqualTo("customer-123");
        assertThat(saved.getTotalAmount()).isEqualByComparingTo("149.99");
        assertThat(saved.getStatus()).isEqualTo("CREATED");
        assertThat(saved.getCreatedAt()).isNotNull();
    }
}

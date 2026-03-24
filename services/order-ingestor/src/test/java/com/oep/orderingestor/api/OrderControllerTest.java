package com.oep.orderingestor.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oep.orderingestor.api.dto.CreateOrderRequest;
import com.oep.orderingestor.application.CreateOrderUseCase;
import com.oep.orderingestor.domain.model.Order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
        private MockMvc mockMvc;
        private ObjectMapper objectMapper = new ObjectMapper();

        @Mock
        private CreateOrderUseCase createOrderUseCase;

        @BeforeEach
        void setup() {
                objectMapper.findAndRegisterModules();
                objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                OrderController controller = new OrderController(createOrderUseCase);
                org.springframework.validation.beanvalidation.LocalValidatorFactoryBean validator = new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
                validator.afterPropertiesSet();

                mockMvc = MockMvcBuilders.standaloneSetup(controller)
                        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                        .setValidator(validator)
                        .build();
        }

    @Test
    void should_return_201_when_order_is_valid() throws Exception {
        // Arrange
        Order created = Order.builder()
                .id(UUID.randomUUID())
                .customerId("customer-123")
                .totalAmount(new BigDecimal("99.99"))
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        when(createOrderUseCase.execute(any())).thenReturn(created);

        CreateOrderRequest request = new CreateOrderRequest(
                "customer-123",
                new BigDecimal("99.99")
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.customerId").value("customer-123"));
    }

    @Test
    void should_return_400_when_customerId_is_blank() throws Exception {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest("", new BigDecimal("99.99"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_totalAmount_is_zero() throws Exception {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-123",
                BigDecimal.ZERO
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

package com.logistic.orders.service.impl;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.entity.Order;
import com.logistic.orders.enums.OrderStatus;
import com.logistic.orders.mapper.OrderMapper;
import com.logistic.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void updateOrderStatus_shouldUpdateSuccessfully_whenTransitionIsValid() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(new OrderDTO());

        OrderDTO result = orderService.updateOrderStatus(id.toString(), OrderStatus.IN_TRANSIT);

        assertNotNull(result);
        assertEquals(OrderStatus.IN_TRANSIT, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_shouldThrowException_whenTransitionIsInvalid() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                orderService.updateOrderStatus(id.toString(), OrderStatus.DELIVERED)
        );

        assertEquals("Transición no permitida: CREATED → DELIVERED", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_shouldThrowException_whenOrderNotFound() {
        UUID id = UUID.randomUUID();

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.updateOrderStatus(id.toString(), OrderStatus.IN_TRANSIT)
        );

        assertEquals("Orden no encontrada", exception.getMessage());
    }
}

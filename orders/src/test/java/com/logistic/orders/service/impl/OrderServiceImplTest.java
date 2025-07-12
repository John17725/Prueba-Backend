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
    void createOrder_shouldCreateSuccessfully() {
        OrderDTO dto = new OrderDTO();
        dto.setOrigin("CDMX");
        dto.setDestination("Monterrey");

        Order entity = new Order();
        entity.setOrigin("CDMX");
        entity.setDestination("Monterrey");

        Order saved = new Order();
        saved.setId(UUID.randomUUID());
        saved.setOrigin("CDMX");
        saved.setDestination("Monterrey");
        saved.setStatus(OrderStatus.CREATED);

        OrderDTO resultDto = new OrderDTO();
        resultDto.setId(saved.getId());
        resultDto.setOrigin(saved.getOrigin());
        resultDto.setDestination(saved.getDestination());
        resultDto.setStatus(saved.getStatus());

        when(orderMapper.toEntity(dto)).thenReturn(entity);
        when(orderRepository.save(entity)).thenReturn(saved);
        when(orderMapper.toDto(saved)).thenReturn(resultDto);

        OrderDTO result = orderService.createOrder(dto);

        assertNotNull(result);
        assertEquals(OrderStatus.CREATED, result.getStatus());
        verify(orderRepository).save(entity);
    }

    @Test
    void getOrderById_shouldReturnOrder_whenFound() {
        UUID id = UUID.randomUUID();
        Order entity = new Order();
        entity.setId(id);
        entity.setOrigin("CDMX");

        OrderDTO dto = new OrderDTO();
        dto.setId(id);
        dto.setOrigin("CDMX");

        when(orderRepository.findById(id)).thenReturn(Optional.of(entity));
        when(orderMapper.toDto(entity)).thenReturn(dto);

        OrderDTO result = orderService.getOrderById(id.toString());

        assertNotNull(result);
        assertEquals("CDMX", result.getOrigin());
    }

    @Test
    void getOrderById_shouldThrowException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            orderService.getOrderById(id.toString());
        });

        assertEquals("Orden no encontrada", ex.getMessage());
    }
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

package com.logistic.orders.service;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;


public interface OrderService {
    OrderDTO createOrder(OrderDTO dto);
    OrderDTO getOrderById(String id);
    List<OrderDTO> listOrders(String status, String origin, String destination, LocalDate from, LocalDate to);
    OrderDTO updateOrderStatus(String id, OrderStatus newStatus);
}

package com.logistic.orders.service.impl;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.entity.Order;
import com.logistic.orders.enums.OrderStatus;
import com.logistic.orders.mapper.OrderMapper;
import com.logistic.orders.repository.OrderRepository;
import com.logistic.orders.service.OrderService;
import com.logistic.orders.util.UUIDValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO){
        Order order = orderMapper.toEntity(orderDTO);
        System.out.println("ORDER => origin: " + orderDTO.getOrigin() +
                ", destination: " + orderDTO.getDestination() +
                ", status: " + orderDTO.getStatus());
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO getOrderById(String id) {
        UUID uuid = UUIDValidator.validate(id);
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDTO> listOrders(String status, String origin, String destination, LocalDate from, LocalDate to) {
        Specification<Order> spec = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (status != null && !status.isEmpty()) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (origin != null && !origin.isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("origin")), "%" + origin.toLowerCase() + "%"));
                }
                if (destination != null && !destination.isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
                }
                if (from != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), java.sql.Timestamp.valueOf(from.atStartOfDay())));
                }
                if (to != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), java.sql.Timestamp.valueOf(to.atTime(23, 59, 59))));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        return orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(String id, OrderStatus newStatus) {
        UUID uuid = UUIDValidator.validate(id);
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        OrderStatus current = order.getStatus(); // ← Ya es enum, no uses valueOf

        if (!isValidTransition(current, newStatus)) {
            throw new IllegalArgumentException("Transición no permitida: " + current + " → " + newStatus);
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }


    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case CREATED -> next == OrderStatus.IN_TRANSIT || next == OrderStatus.CANCELLED;
            case IN_TRANSIT -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            default -> false;
        };
    }

}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO){
        logger.debug("Order origin: {}, to destination: {}, with status: {}",orderDTO.getOrigin(),orderDTO.getDestination(),orderDTO.getStatus());
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        logger.debug("Order created with id: {}", order.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO getOrderById(String id) {
        logger.info("Order by id {} ",id);
        UUID uuid = UUIDValidator.validate(id);
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> {
                    logger.error("Order id {} not found: ", uuid);
                    return new EntityNotFoundException("Orden no encontrada");
                });
        logger.debug("Order id {} found", id);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDTO> listOrders(String status, String origin, String destination, LocalDate from, LocalDate to) {
        logger.debug("Order search with filters- status: {}, origin: {}, destination: {}, from: {}, to: {}",status, origin, destination, from, to);
        Specification<Order> spec = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (status != null && !status.isEmpty()) {
                    logger.debug("Apply filter by status: {}", status);
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (origin != null && !origin.isEmpty()) {
                    logger.debug("Apply filter by origin: {}", origin);
                    predicates.add(cb.like(cb.lower(root.get("origin")), "%" + origin.toLowerCase() + "%"));
                }
                if (destination != null && !destination.isEmpty()) {
                    logger.debug("Apply filter by destination: {}", destination);
                    predicates.add(cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
                }
                if (from != null) {
                    logger.debug("Apply filter by date from: {}", from.toString());
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), java.sql.Timestamp.valueOf(from.atStartOfDay())));
                }
                if (to != null) {
                    logger.debug("Apply filter by date until: {}", to.toString());
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), java.sql.Timestamp.valueOf(to.atTime(23, 59, 59))));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        List<OrderDTO> results = orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::toDto)
                .toList();
        logger.info("Orders found {} with the provided filters", results.size());
        return results;
    }

    @Override
    public OrderDTO updateOrderStatus(String id, OrderStatus newStatus) {
        logger.info("Update order status with id: {}", id);
        UUID uuid = UUIDValidator.validate(id);
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> {
                    logger.error("Order id {} not found: ", uuid);
                    return new EntityNotFoundException("Orden no encontrada");
                });

        OrderStatus current = order.getStatus();
        logger.debug("Order id {} found", id);

        if (!isValidTransition(current, newStatus)) {
            logger.debug("Invalid transition from {} to {}", current, newStatus);
            throw new IllegalArgumentException("Transición no permitida: " + current + " → " + newStatus);
        }
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        logger.debug("Valid transition from {} to {}", current, newStatus);

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

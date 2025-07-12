package com.logistic.orders.service.impl;

import com.logistic.orders.dto.AssignmentDTO;
import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import com.logistic.orders.entity.Driver;
import com.logistic.orders.entity.Order;
import com.logistic.orders.enums.OrderStatus;
import com.logistic.orders.exception.AssignmentException;
import com.logistic.orders.mapper.AssignmentMapper;
import com.logistic.orders.repository.AssignmentRepository;
import com.logistic.orders.repository.DriverRepository;
import com.logistic.orders.repository.OrderRepository;
import com.logistic.orders.service.AssignmentService;
import com.logistic.orders.service.S3Service;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;
    private final AssignmentRepository assignmentRepository;
    private final S3Service s3Service;
    private final AssignmentMapper assignmentMapper;

    private static final Logger logger = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    public AssignmentServiceImpl(OrderRepository orderRepository,
                                 DriverRepository driverRepository,
                                 AssignmentRepository assignmentRepository,
                                 S3Service s3Service,
                                 AssignmentMapper assignmentMapper) {
        this.orderRepository = orderRepository;
        this.driverRepository = driverRepository;
        this.assignmentRepository = assignmentRepository;
        this.s3Service = s3Service;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public AssignmentResponseDTO assignOrder(AssignmentDTO dto) {
        logger.info("Assignment order: {}", dto.getOrderId());
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> {
                    logger.error("Order not found {}",dto.getOrderId());
                    return new AssignmentException("Orden no encontrada");
                });

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            logger.error("Order different status: {} order have: {}",OrderStatus.CREATED,order.getStatus());
            throw new AssignmentException("La orden se encuentra "+order.getStatus());
        }

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> {
                    logger.error("Driver not found {}",dto.getDriverId());
                    return new AssignmentException("Conductor no encontrado");
                });

        if (!driver.isActive()) {
            logger.error("Driver not active id:{} active: {}",driver.getId(),driver.isActive());
            throw new AssignmentException("El conductor no se encuentra activo");
        }

        String documentUrl = s3Service.uploadFile(dto.getDocument(), "documents");
        String imageUrl = s3Service.uploadFile(dto.getImage(), "images");

        Assignment assignment = new Assignment();
        assignment.setOrder(order);
        assignment.setDriver(driver);
        assignment.setDocumentPath(documentUrl);
        assignment.setImagePath(imageUrl);
        assignment.setAssignedAt(LocalDateTime.now());

        assignmentRepository.save(assignment);
        logger.debug("Assignment created with id:{}",assignment.getId());

        order.setStatus(OrderStatus.IN_TRANSIT);
        driver.setActive(false);
        orderRepository.save(order);
        logger.debug("Order success updated with id:{}",order.getId());
        driverRepository.save(driver);
        logger.debug("Driver success status: {}  updated with id:{}",driver.isActive(),driver.getId());

        return assignmentMapper.toDto(assignment);
    }

    @Override
    public Optional<Assignment> findById(UUID id) {
        return assignmentRepository.findById(id);
    }
}
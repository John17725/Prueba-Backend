package com.logistic.orders.service.impl;

import com.logistic.orders.dto.AssignmentDTO;
import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import com.logistic.orders.entity.Driver;
import com.logistic.orders.entity.Order;
import com.logistic.orders.enums.OrderStatus;
import com.logistic.orders.exception.AssignmentException;
import com.logistic.orders.repository.AssignmentRepository;
import com.logistic.orders.repository.DriverRepository;
import com.logistic.orders.repository.OrderRepository;
import com.logistic.orders.mapper.AssignmentMapper;
import com.logistic.orders.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.web.multipart.MultipartFile;

public class AssignmentServiceImplTest {

    private AssignmentRepository assignmentRepository;
    private OrderRepository orderRepository;
    private DriverRepository driverRepository;
    private S3Service s3Service;
    private AssignmentMapper assignmentMapper;
    private AssignmentServiceImpl assignmentService;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        orderRepository = mock(OrderRepository.class);
        driverRepository = mock(DriverRepository.class);
        s3Service = mock(S3Service.class);
        assignmentMapper = mock(AssignmentMapper.class);
        assignmentService = new AssignmentServiceImpl(orderRepository, driverRepository, assignmentRepository, s3Service, assignmentMapper);
    }

    @Test
    void testAssignOrderSuccess() {
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        AssignmentDTO dto = new AssignmentDTO();
        dto.setOrderId(orderId);
        dto.setDriverId(driverId);
        dto.setDocument(mock(MultipartFile.class));
        dto.setImage(mock(MultipartFile.class));

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setActive(true);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));
        when(s3Service.uploadFile(any(), eq("documents"))).thenReturn("doc-url");
        when(s3Service.uploadFile(any(), eq("images"))).thenReturn("img-url");

        Assignment savedAssignment = new Assignment();
        savedAssignment.setId(UUID.randomUUID());
        savedAssignment.setDocumentPath("doc-url");

        when(assignmentRepository.save(any(Assignment.class))).thenReturn(savedAssignment);
        when(assignmentMapper.toDto(any())).thenReturn(new AssignmentResponseDTO(savedAssignment.getId(), savedAssignment.getDocumentPath()));

        AssignmentResponseDTO response = assignmentService.assignOrder(dto);

        assertNotNull(response);
        verify(orderRepository).save(order);
        verify(driverRepository).save(driver);
    }

    @Test
    void testAssignOrderFailsIfOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        AssignmentDTO dto = new AssignmentDTO();
        dto.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        AssignmentException ex = assertThrows(AssignmentException.class, () -> assignmentService.assignOrder(dto));
        assertEquals("Orden no encontrada", ex.getMessage());
    }

    @Test
    void testAssignOrderFailsIfDriverNotFound() {
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);

        AssignmentDTO dto = new AssignmentDTO();
        dto.setOrderId(orderId);
        dto.setDriverId(driverId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        AssignmentException ex = assertThrows(AssignmentException.class, () -> assignmentService.assignOrder(dto));
        assertEquals("Conductor no encontrado", ex.getMessage());
    }

    @Test
    void testAssignOrderFailsIfOrderNotInCreatedStatus() {
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.DELIVERED);

        AssignmentDTO dto = new AssignmentDTO();
        dto.setOrderId(orderId);
        dto.setDriverId(driverId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        AssignmentException ex = assertThrows(AssignmentException.class, () -> assignmentService.assignOrder(dto));
        assertTrue(ex.getMessage().contains("La orden se encuentra"));
    }

    @Test
    void testAssignOrderFailsIfDriverIsInactive() {
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setActive(false);

        AssignmentDTO dto = new AssignmentDTO();
        dto.setOrderId(orderId);
        dto.setDriverId(driverId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driver));

        AssignmentException ex = assertThrows(AssignmentException.class, () -> assignmentService.assignOrder(dto));
        assertEquals("El conductor no se encuentra activo", ex.getMessage());
    }
}
package com.logistic.orders.controller;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.dto.UpdateStatusDTO;
import com.logistic.orders.payload.ApiResponse;
import com.logistic.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        logger.info("Create Order request received: {}",orderDTO);
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        ApiResponse<OrderDTO> response = new ApiResponse<>("Creación exitosa", createdOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable String id) {
        logger.info("Get Order by id request received: {}",id);
        OrderDTO orderDTO = orderService.getOrderById(id);
        ApiResponse<OrderDTO> response = new ApiResponse<>("Orden encontrada", orderDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateStatusDTO statusDTO
    ) {
        logger.info("Update Order status request received: {} to new status: {}",id,statusDTO);
        OrderDTO updated = orderService.updateOrderStatus(id, statusDTO.getStatus());
        return ResponseEntity.ok(new ApiResponse<>("Estado actualizado exitosamente", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> listOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        logger.info("List Orders request received");
        List<OrderDTO> orders = orderService.listOrders(status, origin, destination, from, to);
        return ResponseEntity.ok(new ApiResponse<>("Órdenes filtradas correctamente", orders));
    }
}

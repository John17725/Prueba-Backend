package com.logistic.orders.controller;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.dto.UpdateStatusDTO;
import com.logistic.orders.payload.ApiResponse;
import com.logistic.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Ordenes", description = "Operaciones para crear ordenes, actualizar y obtener detalles")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Crear orden", description = "Registra una nueva orden")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orden creada exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        logger.info("Create Order request received: {}",orderDTO);
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        ApiResponse<OrderDTO> response = new ApiResponse<>("Creación exitosa", createdOrder);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener orden por ID", description = "Devuelve una orden específica por su id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orden encontrada",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable String id) {
        logger.info("Get Order by id request received: {}",id);
        OrderDTO orderDTO = orderService.getOrderById(id);
        ApiResponse<OrderDTO> response = new ApiResponse<>("Orden encontrada", orderDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar estado de una orden", description = "Permite modificar el estado de una orden")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Petición inválida", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateStatusDTO statusDTO
    ) {
        logger.info("Update Order status request received: {} to new status: {}",id,statusDTO);
        OrderDTO updated = orderService.updateOrderStatus(id, statusDTO.getStatus());
        return ResponseEntity.ok(new ApiResponse<>("Estado actualizado exitosamente", updated));
    }

    @Operation(summary = "Listar órdenes", description = "Permite filtrar órdenes por estado, origen, destino y fechas")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Órdenes filtradas correctamente",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content)
    })
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

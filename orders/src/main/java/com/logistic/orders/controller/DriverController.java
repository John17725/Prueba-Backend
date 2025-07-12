package com.logistic.orders.controller;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;
import com.logistic.orders.payload.ApiResponse;
import com.logistic.orders.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Conductores", description = "Operaciones para crear conductores y obtener conductores activos")
@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;
    private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Operation(summary = "Crear conductor", description = "Registra un nuevo conductor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Conductor creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponseDTO>> createDriver(@Valid @RequestBody DriverDTO driverDTO) {
        logger.info("Create driver request: {}", driverDTO.getLicenseNumber());
        DriverResponseDTO createdDriver = driverService.createDriver(driverDTO);
        ApiResponse<DriverResponseDTO> response = new ApiResponse<>("Conductor creado exitosamente", createdDriver);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener conductores activos", description = "Devuelve una lista de conductores activos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conductores encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error del servidor", content = @Content)
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<DriverResponseDTO>>> listActiveDrivers() {
        logger.info("Get drivers active request");
        List<DriverResponseDTO> drivers = driverService.getActiveDrivers();
        ApiResponse<List<DriverResponseDTO>> response = new ApiResponse<>("Conductores activos encontrados", drivers);
        return ResponseEntity.ok(response);
    }
}
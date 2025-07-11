package com.logistic.orders.controller;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;
import com.logistic.orders.payload.ApiResponse;
import com.logistic.orders.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponseDTO>> createDriver(@Valid @RequestBody DriverDTO driverDTO) {
        DriverResponseDTO createdDriver = driverService.createDriver(driverDTO);
        ApiResponse<DriverResponseDTO> response = new ApiResponse<>("Conductor creado exitosamente", createdDriver);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<DriverResponseDTO>>> listActiveDrivers() {
        List<DriverResponseDTO> drivers = driverService.getActiveDrivers();
        ApiResponse<List<DriverResponseDTO>> response = new ApiResponse<>("Conductores activos encontrados", drivers);
        return ResponseEntity.ok(response);
    }
}
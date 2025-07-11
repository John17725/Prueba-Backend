package com.logistic.orders.service;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;

import java.util.List;

public interface DriverService {
    DriverResponseDTO createDriver(DriverDTO driverDTO);
    List<DriverResponseDTO> getActiveDrivers();
}
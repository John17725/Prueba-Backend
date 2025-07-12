package com.logistic.orders.service.impl;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;
import com.logistic.orders.entity.Driver;
import com.logistic.orders.exception.ConflictException;
import com.logistic.orders.mapper.DriverMapper;
import com.logistic.orders.repository.DriverRepository;
import com.logistic.orders.service.DriverService;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private static final Logger logger = LoggerFactory.getLogger(DriverServiceImpl.class);

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverResponseDTO createDriver(DriverDTO driverDTO) {
        if (driverRepository.existsByLicenseNumber(driverDTO.getLicenseNumber())) {
            logger.error("Driver creation failed: duplicate license {}", driverDTO.getLicenseNumber());
            throw new ConflictException("El número de licencia ya existe");
        }

        Driver driver = driverMapper.toEntity(driverDTO);
        driver.setActive(driverDTO.getActive());
        Driver saved = driverRepository.save(driver);
        logger.debug("Driver created with id: {}, active: {}, name: {}, licenseNumber: {}",driver.getId(),driver.isActive(),driver.getName(),driver.getLicenseNumber());
        return driverMapper.toDto(saved);
    }

    @Override
    public List<DriverResponseDTO> getActiveDrivers() {
        List<Driver> activeDrivers = driverRepository.findAllByActiveTrue();
        logger.debug("Fetched {} active drivers", activeDrivers.size());
        return driverMapper.toDtoList(activeDrivers);
    }
}
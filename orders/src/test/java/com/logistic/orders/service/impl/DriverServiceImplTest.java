package com.logistic.orders.service.impl;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;
import com.logistic.orders.entity.Driver;
import com.logistic.orders.exception.ConflictException;
import com.logistic.orders.mapper.DriverMapper;
import com.logistic.orders.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void createDriver_shouldSucceed_whenLicenseIsUnique() {
        DriverDTO dto = new DriverDTO();
        dto.setName("Juan Pérez");
        dto.setLicenseNumber("LFD123456");
        dto.setActive(true);

        Driver entity = new Driver();
        entity.setName("Juan Pérez");
        entity.setLicenseNumber("LFD123456");

        Driver saved = new Driver();
        saved.setId(UUID.randomUUID());
        saved.setName("Juan Pérez");
        saved.setLicenseNumber("LFD123456");
        saved.setActive(true);

        DriverResponseDTO response = new DriverResponseDTO();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setLicenseNumber(saved.getLicenseNumber());
        response.setActive(saved.isActive());

        when(driverRepository.existsByLicenseNumber("LFD123456")).thenReturn(false);
        when(driverMapper.toEntity(dto)).thenReturn(entity);
        when(driverRepository.save(entity)).thenReturn(saved);
        when(driverMapper.toDto(saved)).thenReturn(response);

        DriverResponseDTO result = driverService.createDriver(dto);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("LFD123456", result.getLicenseNumber());
        assertTrue(result.isActive());
    }

    @Test
    void createDriver_shouldThrowConflictException_whenLicenseExists() {
        DriverDTO dto = new DriverDTO();
        dto.setName("Ana");
        dto.setLicenseNumber("LFD123456");
        dto.setActive(true);

        when(driverRepository.existsByLicenseNumber("LFD123456")).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                driverService.createDriver(dto)
        );

        assertEquals("El número de licencia ya existe", exception.getMessage());
        verify(driverRepository, never()).save(any());
    }

    @Test
    void getActiveDrivers_shouldReturnListOfDrivers() {
        Driver driver1 = new Driver();
        driver1.setId(UUID.randomUUID());
        driver1.setName("Pedro");
        driver1.setLicenseNumber("LFD111111");
        driver1.setActive(true);

        Driver driver2 = new Driver();
        driver2.setId(UUID.randomUUID());
        driver2.setName("Luis");
        driver2.setLicenseNumber("LFD222222");
        driver2.setActive(true);

        List<Driver> drivers = Arrays.asList(driver1, driver2);

        DriverResponseDTO dto1 = new DriverResponseDTO();
        dto1.setId(driver1.getId());
        dto1.setName(driver1.getName());
        dto1.setLicenseNumber(driver1.getLicenseNumber());
        dto1.setActive(true);

        DriverResponseDTO dto2 = new DriverResponseDTO();
        dto2.setId(driver2.getId());
        dto2.setName(driver2.getName());
        dto2.setLicenseNumber(driver2.getLicenseNumber());
        dto2.setActive(true);

        when(driverRepository.findAllByActiveTrue()).thenReturn(drivers);
        when(driverMapper.toDtoList(drivers)).thenReturn(Arrays.asList(dto1, dto2));

        List<DriverResponseDTO> result = driverService.getActiveDrivers();

        assertEquals(2, result.size());
        assertEquals("Pedro", result.get(0).getName());
        assertEquals("Luis", result.get(1).getName());
    }
}
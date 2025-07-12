package com.logistic.orders.mapper;

import com.logistic.orders.dto.DriverDTO;
import com.logistic.orders.dto.DriverResponseDTO;
import com.logistic.orders.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    @Mapping(target = "id", ignore = true)
    Driver toEntity(DriverDTO dto);
    DriverResponseDTO toDto(Driver driver);
    List<DriverResponseDTO> toDtoList(List<Driver> drivers);
}

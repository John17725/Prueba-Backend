package com.logistic.orders.mapper;

import com.logistic.orders.dto.AssignmentDetailsDTO;
import com.logistic.orders.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentDetailsMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "documentPath", target = "documentUrl")
    @Mapping(source = "imagePath", target = "imageUrl")
    AssignmentDetailsDTO toDto(Assignment assignment);
}
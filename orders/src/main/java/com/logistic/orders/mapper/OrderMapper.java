package com.logistic.orders.mapper;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderDTO dto);
    OrderDTO toDto(Order entity);
}

package com.logistic.orders.mapper;

import com.logistic.orders.dto.OrderDTO;
import com.logistic.orders.entity.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO dto);
    OrderDTO toDto(Order entity);
}

package com.logistic.orders.mapper;

import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    AssignmentResponseDTO toDto(Assignment assignment);
}
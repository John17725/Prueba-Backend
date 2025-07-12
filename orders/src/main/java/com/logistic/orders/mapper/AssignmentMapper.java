package com.logistic.orders.mapper;

import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(target = "path", ignore = true)
    AssignmentResponseDTO toDto(Assignment assignment);
}
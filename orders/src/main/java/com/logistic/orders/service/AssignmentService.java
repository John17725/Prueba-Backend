package com.logistic.orders.service;


import com.logistic.orders.dto.AssignmentDTO;
import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentService {
    AssignmentResponseDTO assignOrder(AssignmentDTO assignmentDTO);
    Optional<Assignment> findById(UUID id);
}
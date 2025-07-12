package com.logistic.orders.controller;

import com.logistic.orders.dto.AssignmentDTO;
import com.logistic.orders.dto.AssignmentDetailsDTO;
import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import com.logistic.orders.exception.AssignmentException;
import com.logistic.orders.mapper.AssignmentDetailsMapper;
import com.logistic.orders.payload.ApiResponse;

import com.logistic.orders.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentDetailsMapper assignmentDetailsMapper;

    public AssignmentController(AssignmentService assignmentService, AssignmentDetailsMapper assignmentDetailsMapper) {
        this.assignmentService = assignmentService;
        this.assignmentDetailsMapper = assignmentDetailsMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AssignmentResponseDTO>> assignOrder(@Valid @ModelAttribute AssignmentDTO assignmentDTO) {
        AssignmentResponseDTO result = assignmentService.assignOrder(assignmentDTO);
        ApiResponse<AssignmentResponseDTO> response = new ApiResponse<>("Asignación exitosa", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentDetailsDTO>> getAssignment(@PathVariable UUID id) {
        Assignment assignment = assignmentService.findById(id)
                .orElseThrow(() -> new AssignmentException("Asignación no encontrada"));
        AssignmentDetailsDTO dto = assignmentDetailsMapper.toDto(assignment);
        return ResponseEntity.ok(new ApiResponse<>("Asignación encontrada", dto));
    }
}

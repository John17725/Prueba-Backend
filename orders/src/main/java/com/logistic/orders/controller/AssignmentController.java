package com.logistic.orders.controller;

import com.logistic.orders.dto.AssignmentDTO;
import com.logistic.orders.dto.AssignmentDetailsDTO;
import com.logistic.orders.dto.AssignmentResponseDTO;
import com.logistic.orders.entity.Assignment;
import com.logistic.orders.exception.AssignmentException;
import com.logistic.orders.mapper.AssignmentDetailsMapper;
import com.logistic.orders.payload.ApiResponse;
import com.logistic.orders.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Asignaciones", description = "Operaciones para asignar órdenes a conductores")
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentDetailsMapper assignmentDetailsMapper;

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    public AssignmentController(AssignmentService assignmentService, AssignmentDetailsMapper assignmentDetailsMapper) {
        this.assignmentService = assignmentService;
        this.assignmentDetailsMapper = assignmentDetailsMapper;
    }

    @Operation(
            summary = "Asignar una orden a un conductor",
            description = "Permite asignar una orden a un conductor, adjuntando un documento PDF y una imagen. Requiere multipart/form-data."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Asignación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssignmentResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AssignmentResponseDTO>> assignOrder(@Valid @ModelAttribute AssignmentDTO assignmentDTO) {
        logger.info("Request to assign order: {}", assignmentDTO);
        AssignmentResponseDTO result = assignmentService.assignOrder(assignmentDTO);
        ApiResponse<AssignmentResponseDTO> response = new ApiResponse<>("Asignación exitosa", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener detalles de una asignación",
            description = "Consulta una asignación por su ID y devuelve detalles como los url's de archivos asociados y el id de entidades relacionadas."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Asignación encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssignmentDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Asignación no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentDetailsDTO>> getAssignment(@PathVariable UUID id) {
        logger.info("Request to get assign order with id: {}",id);
        Assignment assignment = assignmentService.findById(id)
                .orElseThrow(() -> {
                    logger.error("Assigned order not found with id: {}", id);
                    return new AssignmentException("Asignación no encontrada");
                });
        AssignmentDetailsDTO dto = assignmentDetailsMapper.toDto(assignment);
        logger.debug("Assignment found: {}", dto);
        return ResponseEntity.ok(new ApiResponse<>("Asignación encontrada", dto));
    }
}

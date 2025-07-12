package com.logistic.orders.dto;

import com.logistic.orders.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Datos de una orden")
public class OrderDTO {
    @Schema(description = "ID de la orden", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @NotBlank
    @Schema(description = "Ciudad de origen", example = "CDMX")
    private String origin;

    @NotBlank
    @Schema(description = "Ciudad de destino", example = "Monterrey")
    private String destination;

    @NotNull
    @Schema(description = "Estado de la orden", example = "CREATED")
    private OrderStatus status;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización")
    private LocalDateTime updatedAt;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

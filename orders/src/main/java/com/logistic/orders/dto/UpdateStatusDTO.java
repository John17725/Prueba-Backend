package com.logistic.orders.dto;

import com.logistic.orders.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para actualizar el estado de una orden")
public class UpdateStatusDTO {

    @NotNull
    @Schema(description = "Nuevo estado", example = "ASSIGNED")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

package com.logistic.orders.dto;

import com.logistic.orders.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusDTO {

    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

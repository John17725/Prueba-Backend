package com.logistic.orders.dto;

import java.util.UUID;

public class AssignmentResponseDTO {
    private UUID id;

    public AssignmentResponseDTO(UUID id, String path) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
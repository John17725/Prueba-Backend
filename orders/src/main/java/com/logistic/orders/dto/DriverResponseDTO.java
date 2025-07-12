package com.logistic.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Respuesta con datos del conductor creado")
public class DriverResponseDTO {
    @Schema(description = "ID del conductor", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;
    @Schema(description = "Nombre del conductor", example = "Jesus Gonzalez")
    private String name;
    @Schema(description = "Número de licencia", example = "LFD123456")
    private String licenseNumber;
    @Schema(description = "Estado activo", example = "true")
    private boolean active;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
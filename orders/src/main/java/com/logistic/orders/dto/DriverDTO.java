package com.logistic.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos de un conductor")
public class DriverDTO {
    @Schema(description = "Nombre del conductor", example = "Jesus Gonzalez")
    @NotBlank(message = "Nombre obligatorio")
    private String name;
    @Schema(description = "Número de licencia", example = "LFD123456")
    @NotBlank(message = "Numero de licencia obligatorio")
    @Pattern(
            regexp = "^(?:LFD|[A-Z]{2,3})\\d{5,8}$",
            message = "Número de licencia no válido"
    )
    private String licenseNumber;
    @Schema(description = "Estado activo del conductor", example = "true")
    @NotNull(message = "El campo 'active' es obligatorio")
    private Boolean active;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

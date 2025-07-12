package com.logistic.orders.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class AssignmentDTO {

    @NotNull(message = "Order ID obligatorio")
    private UUID orderId;

    @NotNull(message = "Driver ID obligatorio")
    private UUID driverId;

    @NotNull(message = "Archivo PDF obligatorio")
    private MultipartFile document;

    @NotNull(message = "Imagen obligatoria")
    private MultipartFile image;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }


}
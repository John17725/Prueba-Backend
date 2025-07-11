package com.logistic.orders.util;

import com.logistic.orders.exception.InvalidUUIDException;

import java.util.UUID;

public class UUIDValidator {
    public static UUID validate(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException("UUID inválido: " + id);
        }
    }
}

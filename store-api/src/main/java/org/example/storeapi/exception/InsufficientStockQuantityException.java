package org.example.storeapi.exception;

public class InsufficientStockQuantityException extends RuntimeException {
    public InsufficientStockQuantityException(String message) {
        super(message);
    }
}

package org.example.storeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockQuantityException extends RuntimeException {
    public InsufficientStockQuantityException(String message) {
        super(message);
    }
}

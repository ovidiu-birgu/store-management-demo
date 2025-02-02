package org.example.storeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *     Handle validation errors (e.g., @NotNull, @NotBlank, @DecimalMin)
     *     return a list of errors for each parameter
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Set<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Set<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            // add list of all errors for each field
            if(errors.containsKey(fieldName)) {
                errors.get(fieldName).add(errorMessage);
                errors.put(fieldName, errors.get(fieldName));
            }
            else {
                Set<String> errorMsgs = new HashSet<>();
                errorMsgs.add(errorMessage);
                errors.put(fieldName, errorMsgs);
            }
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

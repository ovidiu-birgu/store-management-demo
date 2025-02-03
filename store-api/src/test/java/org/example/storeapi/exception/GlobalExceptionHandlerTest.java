package org.example.storeapi.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_WhenMultipleErrorsOnFields_ReturnsBadRequestWithFieldErrors() {
        // Create a BindingResult with multiple field errors
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(createFieldError("field1", "must not be null"));
        bindingResult.addError(createFieldError("field1", "must be positive"));
        bindingResult.addError(createFieldError("field2", "must not be empty"));

        // Mock the exception to return the binding result
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Invoke the handler
        ResponseEntity<Map<String, Set<String>>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Set<String>> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(3, errors.get("field1").size()+errors.get("field2").size());
    }

    private FieldError createFieldError(String fieldName, String message) {
        return new FieldError(
                "testObject",
                fieldName,
                null, // rejectedValue
                false, // bindingFailure
                null, // codes
                null, // arguments
                message
        );
    }

    @Test
    void testHandleInsufficientStock() {
        // Arrange
        String errorMessage = "Insufficient stock quantity";
        InsufficientStockQuantityException exception = new InsufficientStockQuantityException(errorMessage);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<String> response = handler.handleInsufficientStock(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}

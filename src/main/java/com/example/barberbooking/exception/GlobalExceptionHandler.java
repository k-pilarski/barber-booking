package com.example.barberbooking.exception;

import com.example.barberbooking.dto.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Błąd walidacji danych wejściowych: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Błąd walidacji formularza. Sprawdź poprawność wprowadzonych danych.",
                errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictExceptions(IllegalStateException ex) {
        log.warn("Konflikt biznesowy: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Wystąpił konflikt biznesowy.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptions(EntityNotFoundException ex) {
        log.warn("Nie znaleziono zasobu: {}", ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Szukany zasób nie istnieje.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        log.warn("Błędny argument: {}", ex.getMessage());
        // Zwykle mapuje się to na 400 lub 404 w zależności od kontekstu. 
        // W tej aplikacji czasem używane dla "Nie znaleziono barbera", więc możemy zwrócić 404 jeśli zaczyna się od "Nie znaleziono"
        HttpStatus status = (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("nie znaleziono")) ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Przekazano niepoprawne dane.",
                null
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentGatewayException(PaymentGatewayException ex) {
        log.error("Błąd bramki płatności: {}", ex.getMessage(), ex);
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllOtherExceptions(Exception ex) {
        log.error("Wystąpił nieoczekiwany błąd serwera: {}", ex.getMessage(), ex);
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Wystąpił wewnętrzny błąd serwera. Prosimy spróbować później.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

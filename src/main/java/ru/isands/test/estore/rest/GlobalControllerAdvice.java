package ru.isands.test.estore.rest;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.exception.PurchaseConflictException;
import ru.isands.test.estore.exception.ResourceNotFoundException;

/**
 * Глобальный обработчик исключений
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handle(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorDTO> handle(InvalidDataAccessApiUsageException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(400, "Invalid request query"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handle(ResourceNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handle(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        String message;
        if (cause instanceof JsonParseException) {
            message = "Invalid JSON format " + cause.getMessage();
        } else {
            message = "Failed to read request body";
        }
        log.warn(message);
        ErrorDTO error = new ErrorDTO(400, message);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(PurchaseConflictException.class)
    public ResponseEntity<ErrorDTO> handle(PurchaseConflictException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(409, e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handle(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(500, "Internal server error"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

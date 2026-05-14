package com.rednorte.ms_reasignacion.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para el microservicio de reasignacion
 * Intercepta las excepciones lanzadas en cualquier controlador y las
 * convierte en respuestas HTTP estructuradas con el formato
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja las excepciones de recurso no encontrados, devuelve HTTP 404
     * @param ex exceocion capturada
     * @return ResponseEntity con estado 404 y detalle del error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Maneja errores de validacion de campos (@Valid), devolviendo HTTP 400
     * @param ex Excepcion de validacion capturada
     * @return ResponseEntity con estado 400 y lista de errores de campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Error de validacion: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(errors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Maneja cualquier excepcion no controlada, devolviendo HTTP 500
     * @param ex excepcion generica capturada
     * @return ResponseEntity con estado 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handlerGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Error interno del servidor")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}

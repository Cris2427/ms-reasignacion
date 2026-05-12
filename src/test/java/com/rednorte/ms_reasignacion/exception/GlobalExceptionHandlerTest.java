package com.rednorte.ms_reasignacion.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link GlobalExceptionHandler}.
 * Verifica que cada handler retorne el status HTTP y mensaje correctos.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleResourceNotFound debe retornar 404 con mensaje del error")
    void handleResourceNotFound_debeRetornar404() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Reasignacion no encontrada con ID: 1");

        ResponseEntity<ErrorResponseDTO> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Reasignacion no encontrada con ID: 1", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("handleValidationErrors debe retornar 400 con lista de errores de campo")
    void handleValidationErrors_debeRetornar400() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "reasignacionRequestDTO");
        bindingResult.addError(new FieldError(
                "reasignacionRequestDTO", "citaId", "El ID de la cita es obligatorio"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("citaId"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("handlerGenericException debe retornar 500 con mensaje generico")
    void handlerGenericException_debeRetornar500() {
        Exception ex = new Exception("Error inesperado de prueba");

        ResponseEntity<ErrorResponseDTO> response = handler.handlerGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("ErrorResponseDTO builder debe construir el objeto correctamente")
    void errorResponseDTO_builder_debeFuncionar() {
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status(404)
                .message("No encontrado")
                .build();

        assertEquals(404, dto.getStatus());
        assertEquals("No encontrado", dto.getMessage());
    }

    @Test
    @DisplayName("ResourceNotFoundException debe guardar el mensaje correctamente")
    void resourceNotFoundException_debeTenerMensaje() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Paciente no encontrado con ID: 99");

        assertEquals("Paciente no encontrado con ID: 99", ex.getMessage());
    }
}
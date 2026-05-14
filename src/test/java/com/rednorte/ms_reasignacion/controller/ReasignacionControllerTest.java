package com.rednorte.ms_reasignacion.controller;

import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.exception.ResourceNotFoundException;
import com.rednorte.ms_reasignacion.service.ReasignacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link ReasignacionController}.
 * Verifica el comportamiento de cada endpoint usando Mockito puro,
 * compatible con Spring Boot 4.x.
 */
@ExtendWith(MockitoExtension.class)
class ReasignacionControllerTest {

    @Mock
    private ReasignacionService reasignacionService;

    @InjectMocks
    private ReasignacionController controller;

    private ReasignacionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = ReasignacionResponseDTO.builder()
                .id(1L)
                .estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST debe retornar 201 con reasignacion creada exitosamente")
    void crearReasignacion_debeRetornar201() {
        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L)
                .pacienteCanceladorId(1L)
                .motivoCancelacion("Viaje")
                .notificarNuevoPaciente(true)
                .build();

        when(reasignacionService.crearReasignacion(any())).thenReturn(responseDTO);

        ResponseEntity<ReasignacionResponseDTO> result = controller.crearReasignacion(request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(EstadoReasignacion.COMPLETADA, result.getBody().getEstado());
        verify(reasignacionService).crearReasignacion(any());
    }

    @Test
    @DisplayName("GET por ID debe retornar 200 con la reasignacion encontrada")
    void obtenerPorId_existente_debeRetornar200() {
        when(reasignacionService.obtenerPorId(1L)).thenReturn(responseDTO);

        ResponseEntity<ReasignacionResponseDTO> result = controller.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1L, result.getBody().getId());
        verify(reasignacionService).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET por ID debe lanzar excepcion cuando no existe")
    void obtenerPorId_noExistente_debeLanzarExcepcion() {
        when(reasignacionService.obtenerPorId(999L))
                .thenThrow(new ResourceNotFoundException("Reasignacion no encontrada con ID: 999"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.obtenerPorId(999L));
    }

    @Test
    @DisplayName("GET listar todas debe retornar 200 con lista completa")
    void listarTodas_debeRetornar200ConLista() {
        List<ReasignacionResponseDTO> lista = List.of(
                ReasignacionResponseDTO.builder().id(1L)
                        .estado(EstadoReasignacion.COMPLETADA)
                        .fechaRegistro(LocalDateTime.now()).build(),
                ReasignacionResponseDTO.builder().id(2L)
                        .estado(EstadoReasignacion.FALLIDA)
                        .fechaRegistro(LocalDateTime.now()).build()
        );

        when(reasignacionService.listarTodas()).thenReturn(lista);

        ResponseEntity<List<ReasignacionResponseDTO>> result = controller.listarTodas();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(reasignacionService).listarTodas();
    }

    @Test
    @DisplayName("GET por estado debe retornar 200 con lista filtrada")
    void listarPorEstado_debeRetornar200ConListaFiltrada() {
        List<ReasignacionResponseDTO> lista = List.of(
                ReasignacionResponseDTO.builder().id(1L)
                        .estado(EstadoReasignacion.PENDIENTE)
                        .fechaRegistro(LocalDateTime.now()).build()
        );

        when(reasignacionService.listarPorEstado(EstadoReasignacion.PENDIENTE))
                .thenReturn(lista);

        ResponseEntity<List<ReasignacionResponseDTO>> result =
                controller.listarPorEstado(EstadoReasignacion.PENDIENTE);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(reasignacionService).listarPorEstado(EstadoReasignacion.PENDIENTE);
    }
}

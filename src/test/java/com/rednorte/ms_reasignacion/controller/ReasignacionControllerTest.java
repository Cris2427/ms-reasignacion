package com.rednorte.ms_reasignacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.exception.ResourceNotFoundException;
import com.rednorte.ms_reasignacion.service.ReasignacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReasignacionController.class)
class ReasignacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReasignacionService reasignacionService;

    @Test
    @DisplayName("POST /api/v1/reasignaciones debe retornar 201 con reasignacion creada")
    void crearReasignacion_debeRetornar201() throws Exception {
        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L).pacienteCanceladorId(1L)
                .motivoCancelacion("Viaje").notificarNuevoPaciente(true).build();

        ReasignacionResponseDTO response = ReasignacionResponseDTO.builder()
                .id(100L).estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now()).build();

        when(reasignacionService.crearReasignacion(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/reasignaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.estado").value("COMPLETADA"));
    }

    @Test
    @DisplayName("GET /api/v1/reasignaciones/{id} debe retornar 200 con la reasignación")
    void obtenerPorId_existente_debeRetornar200()  throws Exception {
        ReasignacionResponseDTO response = ReasignacionResponseDTO.builder()
                .id(1L).estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now()).build();

        when(reasignacionService.obtenerPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/reasignaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("COMPLETADA"));
    }

    @Test
    @DisplayName("GET /api/v1/reasignaciones debe retornar 200 con lista completa")
    void listarTodas_debeRetornar200() throws Exception {
        List<ReasignacionResponseDTO> lista = List.of(
                ReasignacionResponseDTO.builder().id(1L)
                        .estado(EstadoReasignacion.COMPLETADA)
                        .fechaRegistro(LocalDateTime.now()).build(),
                ReasignacionResponseDTO.builder().id(2L)
                        .estado(EstadoReasignacion.FALLIDA)
                        .fechaRegistro(LocalDateTime.now()).build()
        );

        when(reasignacionService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reasignaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/reasignaciones/estado/{estado} debe retornar 200 filtrado")
    void listarPorEstado_debeRetornar200() throws Exception {
        List<ReasignacionResponseDTO> lista = List.of(
                ReasignacionResponseDTO.builder().id(1L)
                        .estado(EstadoReasignacion.PENDIENTE)
                        .fechaRegistro(LocalDateTime.now()).build()
        );

        when(reasignacionService.listarPorEstado(EstadoReasignacion.PENDIENTE))
                .thenReturn(lista);

        mockMvc.perform(get("/api/v1/reasignaciones/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

}

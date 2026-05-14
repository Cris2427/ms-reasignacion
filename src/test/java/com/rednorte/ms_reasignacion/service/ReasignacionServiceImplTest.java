package com.rednorte.ms_reasignacion.service;


import com.rednorte.ms_reasignacion.client.ListaEsperaClient;
import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.mapper.ReasignacionMapper;
import com.rednorte.ms_reasignacion.model.Cita;
import com.rednorte.ms_reasignacion.model.Paciente;
import com.rednorte.ms_reasignacion.model.Reasignacion;
import com.rednorte.ms_reasignacion.repository.CitaRepository;
import com.rednorte.ms_reasignacion.repository.PacienteRepository;
import com.rednorte.ms_reasignacion.repository.ReasignacionRepository;
import com.rednorte.ms_reasignacion.service.impl.ReasignacionServiceImpl;
import com.rednorte.ms_reasignacion.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReasignacionServiceImplTest {
    @Mock private ReasignacionRepository reasignacionRepository;
    @Mock private CitaRepository citaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private ListaEsperaClient listaEsperaClient;
    @Mock private ReasignacionMapper mapper;

    @InjectMocks
    private ReasignacionServiceImpl service;

    @Test
    @DisplayName("Debe crear reasignacion exitosa cuando hay paciente en lista de espera")
    void crearReasignacion_conCandidatoDisponible_debeCompletarse() {
        Cita cita = Cita.builder().id(1L).especialidad("Cardiologia")
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .nombreMedico("Dr. Lopez").estado("CANCELADA").build();

        Paciente cancelador = Paciente.builder()
                .id(1L).nombreCompleto("Juan Perez").rut("12345678-9").build();

        Paciente nuevoPaciente = Paciente.builder()
                .id(2L).nombreCompleto("Pedro Soto").rut("98765432-1").build();

        Reasignacion guardada = Reasignacion.builder()
                .id(100L).cita(cita).pacienteCancelador(cancelador)
                .nuevoPaciente(nuevoPaciente)
                .estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now()).build();

        ReasignacionResponseDTO responseDTO = ReasignacionResponseDTO.builder()
                .id(100L).estado(EstadoReasignacion.COMPLETADA).build();

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(cancelador));
        when(listaEsperaClient.obtenerSiguientePaciente("Cardiologia")).thenReturn(2L);
        when(pacienteRepository.findById(2L)).thenReturn(Optional.of(nuevoPaciente));
        when(reasignacionRepository.save(any())).thenReturn(guardada);
        when(mapper.toResponseDTO(guardada)).thenReturn(responseDTO);

        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L).pacienteCanceladorId(1L)
                .motivoCancelacion("Imprevisto").notificarNuevoPaciente(true).build();

        ReasignacionResponseDTO result = service.crearReasignacion(request);

        assertNotNull(result);
        assertEquals(EstadoReasignacion.COMPLETADA, result.getEstado());
        verify(listaEsperaClient).marcarPacienteAsignado(2L);
        verify(reasignacionRepository).save(any());
    }

    @Test
    @DisplayName("Debe marcar como FALLIDA cuando no hay candidatos en lista de espera")
    void crearReasignacion_sinCandidatos_debeMarcarComoFallida() {
        Cita cita = Cita.builder().id(1L).especialidad("Neurología")
                .fecha(LocalDate.now().plusDays(1))
                .hora(LocalTime.NOON)
                .nombreMedico("Dra. Soto").estado("CANCELADA").build();

        Paciente cancelador = Paciente.builder()
                .id(1L).nombreCompleto("Ana Torres").rut("11111111-1").build();

        Reasignacion guardada = Reasignacion.builder()
                .id(50L).cita(cita).pacienteCancelador(cancelador)
                .estado(EstadoReasignacion.FALLIDA)
                .fechaRegistro(LocalDateTime.now()).build();

        ReasignacionResponseDTO responseDTO = ReasignacionResponseDTO.builder()
                .id(50L).estado(EstadoReasignacion.FALLIDA).build();

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(cancelador));
        when(listaEsperaClient.obtenerSiguientePaciente("Neurología")).thenReturn(null);
        when(reasignacionRepository.save(any())).thenReturn(guardada);
        when(mapper.toResponseDTO(guardada)).thenReturn(responseDTO);

        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L).pacienteCanceladorId(1L).build();

        ReasignacionResponseDTO result = service.crearReasignacion(request);

        assertEquals(EstadoReasignacion.FALLIDA, result.getEstado());
        verify(listaEsperaClient, never()).marcarPacienteAsignado(any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la cita no existe")
    void crearReasignacion_citaNoExiste_debeLanzarExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(99L).pacienteCanceladorId(1L).build();

        assertThrows(ResourceNotFoundException.class,
                () -> service.crearReasignacion(request));
    }

    @Test
    @DisplayName("Debe retornar la reasignación cuando el ID existe")
    void obtenerPorId_existente_debeRetornarDTO() {
        Reasignacion reasignacion = Reasignacion.builder()
                .id(1L).estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now()).build();

        ReasignacionResponseDTO dto = ReasignacionResponseDTO.builder()
                .id(1L).estado(EstadoReasignacion.COMPLETADA).build();

        when(reasignacionRepository.findById(1L)).thenReturn(Optional.of(reasignacion));
        when(mapper.toResponseDTO(reasignacion)).thenReturn(dto);

        ReasignacionResponseDTO result = service.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe")
    void obtenerPorId_noExistente_debeLanzarExcepcion() {
        when(reasignacionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("Debe retornar lista de todas las reasignaciones")
    void listarTodas_debeRetornarListaCompleta() {
        Reasignacion r1 = Reasignacion.builder().id(1L)
                .estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now()).build();
        Reasignacion r2 = Reasignacion.builder().id(2L)
                .estado(EstadoReasignacion.FALLIDA)
                .fechaRegistro(LocalDateTime.now()).build();

        when(reasignacionRepository.findAll()).thenReturn(List.of(r1, r2));
        when(mapper.toResponseDTO(any())).thenReturn(new ReasignacionResponseDTO());

        List<ReasignacionResponseDTO> result = service.listarTodas();

        assertEquals(2, result.size());
        verify(reasignacionRepository).findAll();
    }

    @Test
    @DisplayName("Debe filtrar reasignaciones por estado")
    void listarPorEstado_debeRetornarListaFiltrada() {
        Reasignacion r = Reasignacion.builder().id(1L)
                .estado(EstadoReasignacion.PENDIENTE)
                .fechaRegistro(LocalDateTime.now()).build();

        when(reasignacionRepository.findByEstado(EstadoReasignacion.PENDIENTE))
                .thenReturn(Arrays.asList(r));
        when(mapper.toResponseDTO(any())).thenReturn(new ReasignacionResponseDTO());

        List<ReasignacionResponseDTO> result =
                service.listarPorEstado(EstadoReasignacion.PENDIENTE);

        assertEquals(1, result.size());
        verify(reasignacionRepository).findByEstado(EstadoReasignacion.PENDIENTE);
    }
}

package com.rednorte.ms_reasignacion.service.impl;

import com.rednorte.ms_reasignacion.client.ListaEsperaClient;
import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.exception.ResourceNotFoundException;
import com.rednorte.ms_reasignacion.mapper.ReasignacionMapper;
import com.rednorte.ms_reasignacion.model.Cita;
import com.rednorte.ms_reasignacion.model.Paciente;
import com.rednorte.ms_reasignacion.model.Reasignacion;
import com.rednorte.ms_reasignacion.repository.CitaRepository;
import com.rednorte.ms_reasignacion.repository.PacienteRepository;
import com.rednorte.ms_reasignacion.repository.ReasignacionRepository;
import com.rednorte.ms_reasignacion.service.ReasignacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//Logica negocio, realiza la interaccion entre repos

@Slf4j
@Service
@RequiredArgsConstructor
public class ReasignacionServiceImpl implements ReasignacionService {

    private final ReasignacionRepository reasignacionRepository;
    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final ListaEsperaClient listaEsperaClient;
    private final ReasignacionMapper mapper;

    /**
     * Valida que la cita y el paciente cancelador existan
     * consulta al ms-lista-espera el siguiente candidato
     * si hay candidato asigna y marca como COMPLETADA
     * si no hay candidatomarca como FALLIDA
     */
    @Override
    @Transactional
    public ReasignacionResponseDTO crearReasignacion(ReasignacionRequestDTO request) {
        log.info("Iniciando reasignacion para cita ID: {}", request.getCitaId());

        Cita cita = citaRepository.findById(request.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cita no encontrada con ID: " + request.getCitaId()));

        Paciente pacienteCancelador = pacienteRepository.findById(request.getPacienteCanceladorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente no encontrado con ID: " + request.getPacienteCanceladorId()));

        Reasignacion reasignacion = Reasignacion.builder()
                .cita(cita)
                .pacienteCancelador(pacienteCancelador)
                .estado(EstadoReasignacion.PENDIENTE)
                .motivoCancelacion(request.getMotivoCancelacion())
                .notificarNuevoPaciente(request.getNotificarNuevoPaciente())
                .build();

        try {
            Long nuevoPacienteId = listaEsperaClient
                    .obtenerSiguientePaciente(cita.getEspecialidad());

            if (nuevoPacienteId != null) {
                Paciente nuevoPaciente = pacienteRepository.findById(nuevoPacienteId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Nuevo paciente no encontrado con ID: " + nuevoPacienteId));

                reasignacion.setNuevoPaciente(nuevoPaciente);
                reasignacion.setEstado(EstadoReasignacion.COMPLETADA);
                reasignacion.setFechaResolucion(LocalDateTime.now());

                listaEsperaClient.marcarPacienteAsignado(nuevoPacienteId);
                log.info("Reasignacion completada. Nuevo paciente ID: {}", cita.getEspecialidad());
            } else {
                reasignacion.setEstado(EstadoReasignacion.FALLIDA);
                reasignacion.setFechaResolucion(LocalDateTime.now());
                log.warn("Sin candidatos en lista de espera para: {}", cita.getEspecialidad());
            }
        } catch (Exception e) {
            reasignacion.setEstado(EstadoReasignacion.FALLIDA);
            reasignacion.setFechaResolucion(LocalDateTime.now());
            log.error("Error al consultar lista de espera: {}", e.getMessage());
        }
        Reasignacion guardada = reasignacionRepository.save(reasignacion);
        return mapper.toResponseDTO(guardada);
    }

    /**
     *
     * @throws RuntimeException si no existe la reasignacion con ese ID
     */

    @Override
    @Transactional(readOnly = true)
    public ReasignacionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando reasignacion con ID: {}", id);
        Reasignacion reasignacion = reasignacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reasignacion no encontrada con ID: " + id));
        return mapper.toResponseDTO(reasignacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReasignacionResponseDTO> listarTodas() {
        log.info("Listando todas las reasignaciones");
        return reasignacionRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReasignacionResponseDTO> listarPorEstado(EstadoReasignacion estado) {
        log.info("Listando reasignaciones con estado: {}", estado);
        return reasignacionRepository.finByEstado(estado)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

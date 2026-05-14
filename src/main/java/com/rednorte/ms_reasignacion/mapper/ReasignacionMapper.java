package com.rednorte.ms_reasignacion.mapper;

import com.rednorte.ms_reasignacion.dto.CitaDTO;
import com.rednorte.ms_reasignacion.dto.PacienteDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.model.Cita;
import com.rednorte.ms_reasignacion.model.Paciente;
import com.rednorte.ms_reasignacion.model.Reasignacion;
import org.springframework.stereotype.Component;

@Component
public class ReasignacionMapper {
    /**
     * convierte una entidad en su DTO de respuesta
     * @param reasignacion entidad que convierte
     * @return DTO listo para la respuesta HTtP
     */
    public ReasignacionResponseDTO toResponseDTO(Reasignacion reasignacion) {
        return ReasignacionResponseDTO.builder()
                .id(reasignacion.getId())
                .cita(toCitaDTO(reasignacion.getCita()))
                .pacienteCancelador(toPacienteDTO(reasignacion.getPacienteCancelador()))
                .nuevoPaciente(reasignacion.getNuevoPaciente() != null
                        ? toPacienteDTO(reasignacion.getNuevoPaciente())
                        : null)
                .estado(reasignacion.getEstado())
                .fechaRegistro(reasignacion.getFechaRegistro())
                .fechaResolucion(reasignacion.getFechaResolucion())
                .motivoCancelacion(reasignacion.getMotivoCancelacion())
                .build();
    }

    /**
     * convierte una entidad en DTO de transferencia
     * @param cita entidad que convierte
     * @return DTO de la cita o null si es nula
     */
    public CitaDTO toCitaDTO(Cita cita) {
        if (cita == null) return null;
        return CitaDTO.builder()
                .id(cita.getId())
                .fecha(cita.getFecha())
                .hora(cita.getHora())
                .nombreMedico(cita.getNombreMedico())
                .especialidad(cita.getEspecialidad())
                .centroSalud(cita.getCentroSalud())
                .estado(cita.getEstado())
                .build();
    }

    /**
     * convierte una entidad en DTO de transferencia
     * @param paciente entidad que convierte
     * @return DTO del paciente y null si es nulo
     */
    public PacienteDTO toPacienteDTO(Paciente paciente) {
        if (paciente == null) return null;
        return PacienteDTO.builder()
                .id(paciente.getId())
                .nombreCompleto(paciente.getNombreCompleto())
                .rut(paciente.getRut())
                .gmail(paciente.getGmail())
                .telefono(paciente.getTelefono())
                .build();
    }
}

package com.rednorte.ms_reasignacion.repository;


import com.rednorte.ms_reasignacion.model.Reasignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReasignacionRepository extends JpaRepository<Reasignacion, Long> {
    /**
     * Obtiene todos los registros  de reasignaicon por su estado actual
     * @param estado estado a filtrar
     * @return Lista de reasignaciones con el estado indicado
     */
    List<Reasignacion> finByEstado(EstadoReasignacion estado);
    /**
     * Obtiene el hisotiral de reasignaciones asociadas a una cita especifica
     * @param citaId ID de la cita medica
     * @return Lista de reasignacion vinculadas a esa citaa
     */
    List<Reasignacion> findByCitaId(Long citaId);
    /**
     * Obtiene todas las asignaciones donde el paciente dado figura como el nuevo beneficiario asignado
     * @param nuevoPaciente id del nuevo paciente asignado
     * @return lista de reasignaciones asignadas a ese paciente
     */
    List<Reasignacion> finByNuevoPacienteId(Long nuevoPaciente);
    /**
     * Obtiene el historial de cancelaciones realizadas por un paciente
     * @param pacienteCanceladorId id del paciente que cancelo
     * @return lista de reasignaciones originadas por ese pacietne
     */
    List<Reasignacion> findByPacienteCancelador(Long pacienteCanceladorId);
}

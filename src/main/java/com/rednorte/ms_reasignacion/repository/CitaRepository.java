package com.rednorte.ms_reasignacion.repository;

import com.rednorte.ms_reasignacion.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    /**
     * se obtienen todas las citas filtradas por especialidad medica
     * @param especialidad
     * @return
     */
    List<Cita> findByEspecialidad(String especialidad);
    /**
     * obtiene todas las citas segun su estado actual
     * @param estado
     * @return
     */
    List<Cita>findByEstado(String estado);

    /**
     * busca citas disponibles por especialidad
     * sirce para encontrar slots libres al momento de reasignar
     * @param especialidad
     * @param estado
     * @return
     */
    List<Cita> findByEspecialidadAndEstado(String especialidad, String estado);
}

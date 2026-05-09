package com.rednorte.ms_reasignacion.repository;

import com.rednorte.ms_reasignacion.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    /**
     * Busca paciente por el rut
     * @param rut Rut del paciente en formato sin puntos
     * @return link Optional con el paciente encontrado, o vacio si no existe
     */
    Optional<Paciente> findByRut(String rut);

    /**
     * verifica si hay un paciente creado con el rut que se dio
     * @param rut Rut a verificar
     * @return code true si el rut ya esta registrado
     */
    boolean existsByRut(String rut);
}

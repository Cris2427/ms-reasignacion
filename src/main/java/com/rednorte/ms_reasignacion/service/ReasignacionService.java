package com.rednorte.ms_reasignacion.service;

import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;

import java.util.List;


public interface ReasignacionService {
    /**
     * Registra y procesa una solicitud nueva de reaasignacion
     * @param request DTO con los datos de la cita
     * @return DTO con el resultado completo
     */
    ReasignacionResponseDTO crearReasignacion(ReasignacionRequestDTO request);

    /**
     * Obtiene el detalle de una reasignacion por su ID
     * @param id ID de la reasignacion
     * @return DTO con los datos encontrados
     */
    ReasignacionResponseDTO obtenerPorId(Long id);

    /**
     * Lista todas las reasignaciones registradas en el sistema
     * @return Lista ocmpleta de reasignaciones
     */
    List<ReasignacionResponseDTO> listarTodas();

    /**
     * Lista las readignaciones filtradas por el estado
     *
     * @param estado estado por el cual filtra
     * @return lista de reasignacion con el estado indicado
     */

    List<ReasignacionResponseDTO> listarPorEstado(EstadoReasignacion estado);
}

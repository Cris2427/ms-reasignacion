package com.rednorte.ms_reasignacion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.nio.file.Path;

@FeignClient(name = "ms-lista-espera", url = "${ms.lista-espera.url}")
public interface ListaEsperaClient {
    /**
     *
     * @param especialidad nombre de especialidad
     * @return id del paciente reasignado
     */
    @GetMapping("/api/v1/lista-espera/siguiente/{especialida}")
    Long obtenerSiguientePaciente(@PathVariable("especialidad") String especialidad);

    /**
     *
     * @param pacienteId id del paciente que fue asignado exitosamente
     */
    @PutMapping("/api/v1/lista-espera/asignar/{pacienteId}")
    void marcarPacienteAsignado(@PathVariable("pacienteId") Long pacienteId);
}

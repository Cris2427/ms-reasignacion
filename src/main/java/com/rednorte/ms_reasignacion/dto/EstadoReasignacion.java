package com.rednorte.ms_reasignacion.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado del proceso de reasignacion de una cita medica")
public enum EstadoReasignacion {
    PENDIENTE,
    COMPLETADA,
    FALLIDA,
    CANCELADA
}

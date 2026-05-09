package com.rednorte.ms_reasignacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReasignacionResponseDTO", description = "Resultado completo del proceso de reasignacion automatica")

public class ReasignacionRespondeDTO {

    @Schema(description = "ID unico del registro de reasignacion", example = "200")
    private Long id;

    @Schema(description = "Datos completos de la cita reasignada")
    private CitaDTO cita;

    @Schema(description = "Datos del paciente que cancelo")
    private PacienteDTO pacienteCancelador;

    @Schema(description = "Datos del nuevo paciente asignado")
    private PacienteDTO nuevoPaciente;

    @Schema(description = "Estado actual del proceso", example = "COMPLETADA")
    private EstadoReasignacion estado;

    @Schema(description = "Fecha y hora de registro", example = "2026-06-10T14:30:00")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Fecha y hora de resolucion", example = "2026-06-10T14:31:05")
    private  LocalDateTime fechaResolucion;

    @Schema(description = "Motivo de la cancelacion original")
    private String motivoCancelacion;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Cita reasignada exitosamente")
    private String mensaje;
}

package com.rednorte.ms_reasignacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CitaDTO", description = "Datos de una cita medica disponible para reasignacion")
public class CitaDTO {

    @Schema(description = "Identificador unico de la cita", example = "101")
    private Long id;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser futura")
    @Schema(description = "Fecha de la cita(yyyy-MM-dd)", example = "2205-07-15")
    private LocalDate fecha;

    @NotNull(message = "La hora de la cita es obligatoria")
    @Schema(description = "Hora de inicio de la cita(HH:mm)", example = "09:30")
    private LocalTime hora;

    @NotBlank(message = "El nombre del medico es obligatoria")
    @Schema(description = "Nombre completo del medico", example = "Dra. Floriponcia Guadalupe")
    private String nombreMedico;

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(description = "Nombre de la especialidad", example = "Cardiologia")
    private String especialidad;

    @Schema(description = "Centro de salud", example = "Centro Medico Norte")
    private String centroSalud;

    @Schema(description = "Estado de la cita", example = "DISPONIBLE")
    private String estado;
}

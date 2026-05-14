package com.rednorte.ms_reasignacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PacienteDTO", description = "Datos de identificacion de un paciente")
public class PacienteDTO {

    @Schema(description = "Idetinficacion unica del paciente", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Schema(description = "Nombre completo del paciente", example = "Juan Perez Gonzales")
    private String nombreCompleto;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "\\d{7,8}-[\\dkK]", message = "Formato de RUT invalido")
    @Schema(description = "RUT del paciente sin puntos", example = "12345678-9")
    private String rut;

    @Email(message = "El gmail debe tener un formato valido")
    @Schema(description = "Correo del paciente", example = "juan.perez@gmail.com")
    private String gmail;

    @Schema(description = "Telefono de contacto", example = "+56964026854")
    private String telefono;
}

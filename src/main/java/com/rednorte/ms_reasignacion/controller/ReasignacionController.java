package com.rednorte.ms_reasignacion.controller;

import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.ReasignacionRequestDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.service.ReasignacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reasignaciones")
@RequiredArgsConstructor
@Tag(
        name = "Reasignaciones",
        description = "Endpoints para gestionar la reasignación automética de citas médicas canceladas"
)
public class ReasignacionController {

    private final ReasignacionService reasignacionService;

    /**
     * Crea y reasigna una nueva reasignacion para una cita cancelada
     * @param request DTO con los datos de la cita cancelada y el paciente
     * @return ResponseEntity con el resultado del proceso y estado HTTP 201
     */

    @Operation(
            summary = "Crear reasignación",
            description = "Inicia el proceso automatico de reasignacion para una cita medica cancelada. " +
                    "Consulta la lista de espera y asigna la cita al siguiente paciente elegible.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reasignacion creada exitosamente",
                content = @Content(schema = @Schema(implementation = ReasignacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos",
                content = @Content),
            @ApiResponse(responseCode = "404", description = "Cita o paciente no encontrado",
                content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReasignacionResponseDTO> crearReasignacion(
            @Valid @RequestBody ReasignacionRequestDTO request) {
        ReasignacionResponseDTO response = reasignacionService.crearReasignacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene el detalle de una reasignacion por su ID
     * @param id ID de la reasignacion a consultar
     * @return ResponseEntity con los datos de la reasignacion
     */

    @Operation(
            summary = "Obtener reasignación por ID",
            description = "Retorna el detalle completo de una reasignación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reasignacion encontrada",
                content = @Content(schema = @Schema(implementation = ReasignacionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reasignacion no encontrada",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReasignacionResponseDTO> obtenerPorId(
            @Parameter(description = "ID unico de la reasignacion", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(reasignacionService.obtenerPorId(id));
    }

    /**
     * Listar todas las reasignaciones registradas en ek sistema
     * @return Repsonse Entity con la lista completa de reasignaciones
     */
    @Operation(
            summary = "Listar todas las reasignaciones",
            description = "Retorna el historial completo de las reasignaciones del sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
        content = @Content(schema = @Schema(implementation = ReasignacionResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<ReasignacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok((reasignacionService.listarTodas()));
    }

    /**
     * Lista las reasignaciones fisltrada por su estado actual
     * @param estado Estado por el que desea filtrar
     * @return ResponseEntity con la lista filtrada de reasginaciones
     */

    @Operation(
            summary = "Listar reasignaciones por estado",
            description = "Filtra y retorna las reasignaciones segun su estado: " + "PENDIENTE, COMPLETADA, FALLIDA o CANCELADA."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = ReasignacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido",
                content = @Content)
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReasignacionResponseDTO>> listarPorEstado(
            @Parameter(description = "Estado de la reasignacion",
                schema = @Schema(implementation = EstadoReasignacion.class))
            @PathVariable EstadoReasignacion estado) {
        return ResponseEntity.ok(reasignacionService.listarPorEstado(estado));
    }

}

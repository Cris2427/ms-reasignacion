package com.rednorte.ms_reasignacion.model;

import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reasignacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Reasignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_cancelador_id", nullable = false)
    private Paciente pacienteCancelador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nuevo_paciente_id")
    private Paciente nuevoPaciente;

    /**
     * Estado actual del proceso de reasignacion
     *
     * @see EstadoReasignacion
     */

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReasignacion estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "motivo_cancelacion", length = 500)
    private String motivoCancelacion;

    @Column(name = "notificar_nuevo_paciente")
    private Boolean notificarNuevoPaciente;

    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoReasignacion.PENDIENTE;
        }
    }
}

package com.rednorte.ms_reasignacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "nombre_medico", nullable = false, length = 150)
    private String nombreMedico;

    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "centro_salud", length = 150)
    private String centroSalud;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
}

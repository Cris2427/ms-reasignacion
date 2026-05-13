package com.rednorte.ms_reasignacion.model;

import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReasignacionesModelTest {

    @Test
    @DisplayName("Paciente debe construirse correctamente con builder")
    void paciente_debeConstruirseConBuilder() {
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .rut("12345678-9")
                .gmail("juan@mail.com")
                .telefono("+56912345678")
                .build();

        assertEquals(1L, paciente.getId());
        assertEquals("Juan Perez", paciente.getNombreCompleto());
        assertEquals("12345678-9", paciente.getRut());
        assertEquals("juan@mail.com", paciente.getGmail());
        assertEquals("+56912345678", paciente.getTelefono());
    }

    @Test
    @DisplayName("Paciente debe permitir setters correctamente")
    void paciente_debePermitirSetters() {
        Paciente paciente = new Paciente();
        paciente.setId(2L);
        paciente.setNombreCompleto("Maria Gonzalez");
        paciente.setRut("98765432-1");

        assertEquals(2L, paciente.getId());
        assertEquals("Maria Gonzalez", paciente.getNombreCompleto());
        assertEquals("98765432-1", paciente.getRut());
    }

    @Test
    @DisplayName("Cita debe construirse correctamente con builder")
    void cita_debeConstruirseConBuilder() {
        LocalDate fecha = LocalDate.now().plusDays(5);
        LocalTime hora = LocalTime.of(10, 30);

        Cita cita = Cita.builder()
                .id(10L)
                .fecha(fecha)
                .hora(hora)
                .nombreMedico("Dr. Lopez")
                .especialidad("Cardiologia")
                .centroSalud("Centro Norte")
                .estado("DISPONIBLE")
                .build();

        assertEquals(10L, cita.getId());
        assertEquals(fecha, cita.getFecha());
        assertEquals(hora, cita.getHora());
        assertEquals("Dr. Lopez", cita.getNombreMedico());
        assertEquals("Cardiologia", cita.getEspecialidad());
        assertEquals("DISPONIBLE", cita.getEstado());
    }

    @Test
    @DisplayName("Reasignacion debe tener estado PENDIENTE por defecto via prePersist")
    void reasignacion_prePersist_debeAsignarEstadoPendiente() {
        Reasignacion reasignacion = new Reasignacion();
        reasignacion.prePersist();

        assertEquals(EstadoReasignacion.PENDIENTE, reasignacion.getEstado());
        assertNotNull(reasignacion.getFechaRegistro());
    }

    @Test
    @DisplayName("Reasignacion prePersist no debe sobreescribir estado ya asignado")
    void reasignacion_prePersist_noDebesobreescribirEstado() {
        Reasignacion reasignacion = new Reasignacion();
        reasignacion.setEstado(EstadoReasignacion.COMPLETADA);
        reasignacion.prePersist();

        assertEquals(EstadoReasignacion.COMPLETADA, reasignacion.getEstado());
    }

    @Test
    @DisplayName("Reasignacion debe construirse correctamente con builder")
    void reasignacion_debeConstruirseConBuilder() {
        Paciente paciente = Paciente.builder().id(1L).build();
        Cita cita = Cita.builder().id(1L).build();

        Reasignacion reasignacion = Reasignacion.builder()
                .id(100L)
                .cita(cita)
                .pacienteCancelador(paciente)
                .estado(EstadoReasignacion.PENDIENTE)
                .notificarNuevoPaciente(true)
                .build();

        assertEquals(100L, reasignacion.getId());
        assertEquals(EstadoReasignacion.PENDIENTE, reasignacion.getEstado());
        assertTrue(reasignacion.getNotificarNuevoPaciente());
    }
}
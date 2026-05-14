package com.rednorte.ms_reasignacion.mapper;


import com.rednorte.ms_reasignacion.dto.CitaDTO;
import com.rednorte.ms_reasignacion.dto.EstadoReasignacion;
import com.rednorte.ms_reasignacion.dto.PacienteDTO;
import com.rednorte.ms_reasignacion.dto.ReasignacionResponseDTO;
import com.rednorte.ms_reasignacion.model.Cita;
import com.rednorte.ms_reasignacion.model.Paciente;
import com.rednorte.ms_reasignacion.model.Reasignacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReasignacionMapperTest {
    private ReasignacionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReasignacionMapper();
    }

    @Test
    @DisplayName("Debe convertir Reasignaciona ReasignacionResponseDTO correctamente")
    void toResponseDTO_debeMapearCamposCorrectamente() {

        Paciente cancelador = Paciente.builder()
                .id(1L).nombreCompleto("Juan Perez")
                .rut("12345678-9").gmail("juan@gmail.com").build();

        Paciente nuevo = Paciente.builder()
                .id(2L).nombreCompleto("Pedro Soto")
                .rut("98765432-1").gmail("pedro@gmail.com").build();

        Cita cita = Cita.builder()
                .id(10L).fecha(LocalDate.now().plusDays(5))
                .hora(LocalTime.of(9, 30)).nombreMedico("Dr. López")
                .especialidad("Cardiologia").estado("DISPONIBLE").build();

        Reasignacion reasignacion = Reasignacion.builder()
                .id(100L).cita(cita)
                .pacienteCancelador(cancelador).nuevoPaciente(nuevo)
                .estado(EstadoReasignacion.COMPLETADA)
                .fechaRegistro(LocalDateTime.now())
                .motivoCancelacion("Imprevisto").build();

        ReasignacionResponseDTO dto = mapper.toResponseDTO(reasignacion);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(EstadoReasignacion.COMPLETADA, dto.getEstado());
        assertEquals("Juan Perez", dto.getPacienteCancelador().getNombreCompleto());
        assertEquals("Pedro Soto", dto.getNuevoPaciente().getNombreCompleto());
        assertEquals("Cardiologia", dto.getCita().getEspecialidad());
    }

    @Test
    @DisplayName("Debe retornar null en nuevoPaciente cuando la entidad no tiene nuevo paciente")
    void toResponseDTO_sinNuevoPaciente_debeRetornarNull() {
        Cita cita = Cita.builder().id(1L).especialidad("Traumatologia")
                .fecha(LocalDate.now().plusDays(1)).hora(LocalTime.NOON)
                .nombreMedico("Dr. Garcia").estado("CANCELADA").build();

        Paciente cancelador = Paciente.builder()
                .id(1L).nombreCompleto("Ana Torres").rut("11111111-1").build();

        Reasignacion reasignacion = Reasignacion.builder()
                .id(1L).cita(cita).pacienteCancelador(cancelador)
                .nuevoPaciente(null).estado(EstadoReasignacion.FALLIDA)
                .fechaRegistro(LocalDateTime.now()).build();

        ReasignacionResponseDTO dto = mapper.toResponseDTO(reasignacion);

        assertNull(dto.getNuevoPaciente());
        assertEquals(EstadoReasignacion.FALLIDA, dto.getEstado());
    }

    @Test
    @DisplayName("toCitaDTO debe retornar null si la cita es null")
    void toCitaDTO_conNull_debeRetornarNull() {
        CitaDTO result = mapper.toCitaDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("toPacienteDTO debe retornar null si el paciente es null")
    void toPacienteDTO_conNull_debeRetornarNull() {
        PacienteDTO result = mapper.toPacienteDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("toPacienteDTO debe mapear todos los campos del paciente")
    void toPacienteDTO_debeMappearTodosLosCampos() {
        Paciente paciente = Paciente.builder()
                .id(5L).nombreCompleto("Maria Gonzalez")
                .rut("22222222-2").gmail("maria@gmail.com")
                .telefono("+56999999999").build();

        PacienteDTO dto = mapper.toPacienteDTO(paciente);

        assertEquals(5L, dto.getId());
        assertEquals("Maria Gonzalez", dto.getNombreCompleto());
        assertEquals("22222222-2", dto.getRut());
        assertEquals("maria@gmail.com", dto.getGmail());
        assertEquals("+56999999999", dto.getTelefono());
    }
}

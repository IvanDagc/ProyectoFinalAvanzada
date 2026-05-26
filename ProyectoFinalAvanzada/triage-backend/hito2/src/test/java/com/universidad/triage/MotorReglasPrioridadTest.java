package com.universidad.triage;

import com.universidad.triage.entity.Prioridad;
import com.universidad.triage.entity.TipoSolicitud;
import com.universidad.triage.motor.MotorReglasPrioridad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para el MotorReglasPrioridad.
 * Verifica que las reglas de negocio calculen correctamente la prioridad.
 */
class MotorReglasPrioridadTest {

    private MotorReglasPrioridad motor;

    @BeforeEach
    void setUp() {
        motor = new MotorReglasPrioridad();
    }

    @Test
    @DisplayName("Regla 1: Fecha límite <= 3 días → ALTA siempre")
    void fechaLimiteMuyProxima_deberiaSerALTA() {
        LocalDate manana = LocalDate.now().plusDays(1);

        Prioridad resultado = motor.calcular(
                TipoSolicitud.CONSULTA_ACADEMICA, // tipo NO crítico
                false,                             // sin impacto académico
                manana);                            // fecha muy próxima

        assertThat(resultado).isEqualTo(Prioridad.ALTA);
    }

    @Test
    @DisplayName("Regla 2: Impacto + tipo crítico → ALTA")
    void impactoYTipoCritico_deberiaSerALTA() {
        Prioridad resultado = motor.calcular(
                TipoSolicitud.HOMOLOGACION, // tipo crítico
                true,                        // con impacto académico
                null);                       // sin fecha límite

        assertThat(resultado).isEqualTo(Prioridad.ALTA);
    }

    @Test
    @DisplayName("Regla 3a: Solo impacto académico → MEDIA")
    void soloImpactoAcademico_deberiaSerMEDIA() {
        Prioridad resultado = motor.calcular(
                TipoSolicitud.CONSULTA_ACADEMICA, // tipo NO crítico
                true,                              // con impacto académico
                null);

        assertThat(resultado).isEqualTo(Prioridad.MEDIA);
    }

    @Test
    @DisplayName("Regla 3b: Solo tipo crítico → MEDIA")
    void soloTipoCritico_deberiaSerMEDIA() {
        Prioridad resultado = motor.calcular(
                TipoSolicitud.REGISTRO_ASIGNATURA, // tipo crítico
                false,                              // sin impacto académico
                null);

        assertThat(resultado).isEqualTo(Prioridad.MEDIA);
    }

    @Test
    @DisplayName("Regla 4: Sin factores críticos → BAJA")
    void sinFactoresCriticos_deberiaSerBAJA() {
        Prioridad resultado = motor.calcular(
                TipoSolicitud.CONSULTA_ACADEMICA, // tipo NO crítico
                false,                             // sin impacto
                null);                             // sin fecha límite

        assertThat(resultado).isEqualTo(Prioridad.BAJA);
    }

    @Test
    @DisplayName("Fecha límite en el futuro lejano NO activa urgencia")
    void fechaLimiteLejana_noDeberiaActivarUrgencia() {
        LocalDate mesProximo = LocalDate.now().plusMonths(1);

        Prioridad resultado = motor.calcular(
                TipoSolicitud.CONSULTA_ACADEMICA,
                false,
                mesProximo);

        assertThat(resultado).isEqualTo(Prioridad.BAJA);
    }

    @Test
    @DisplayName("La justificación generada no debe estar vacía")
    void generarJustificacion_noDebeEstarVacia() {
        Prioridad prioridad = Prioridad.ALTA;

        String justificacion = motor.generarJustificacion(
                TipoSolicitud.HOMOLOGACION,
                true,
                null,
                prioridad);

        assertThat(justificacion).isNotBlank();
        assertThat(justificacion).contains("ALTA");
    }
}

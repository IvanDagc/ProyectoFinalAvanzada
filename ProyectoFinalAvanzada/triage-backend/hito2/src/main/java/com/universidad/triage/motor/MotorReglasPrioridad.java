package com.universidad.triage.motor;

import com.universidad.triage.entity.Prioridad;
import com.universidad.triage.entity.TipoSolicitud;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Motor de reglas de negocio para calcular la prioridad de una solicitud.
 *
 * RF-03: La prioridad se asigna con base en:
 *   - Tipo de solicitud
 *   - Impacto académico
 *   - Fecha límite asociada
 *
 * CORRECCIÓN FEEDBACK: El frontend solo envía los insumos (impactoAcademico,
 * fechaLimite). El backend calcula la prioridad aquí. Esto elimina la
 * redundancia de datos en el endpoint /clasificar.
 *
 * Reglas implementadas (de mayor a menor precedencia):
 *   1. Si la fecha límite es <= 3 días → ALTA (urgencia temporal)
 *   2. Si tiene impacto académico Y tipo crítico → ALTA
 *   3. Si tiene impacto académico O tipo crítico → MEDIA
 *   4. En cualquier otro caso → BAJA
 */
@Component
public class MotorReglasPrioridad {

    // Tipos de solicitud que se consideran críticos por naturaleza
    private static final java.util.Set<TipoSolicitud> TIPOS_CRITICOS = java.util.Set.of(
            TipoSolicitud.REGISTRO_ASIGNATURA,
            TipoSolicitud.HOMOLOGACION,
            TipoSolicitud.CANCELACION_ASIGNATURA
    );

    private static final int DIAS_URGENCIA = 3;

    /**
     * Calcula la prioridad de una solicitud según las reglas de negocio.
     *
     * @param tipo            Tipo de solicitud clasificado
     * @param impactoAcademico Si la solicitud afecta directamente el proceso académico
     * @param fechaLimite     Fecha límite opcional
     * @return Prioridad calculada: ALTA, MEDIA o BAJA
     */
    public Prioridad calcular(TipoSolicitud tipo, Boolean impactoAcademico, LocalDate fechaLimite) {
        boolean esUrgentePorFecha = esUrgentePorFecha(fechaLimite);
        boolean esTipoCritico = TIPOS_CRITICOS.contains(tipo);
        boolean tieneImpacto = Boolean.TRUE.equals(impactoAcademico);

        // Regla 1: Urgencia temporal absoluta
        if (esUrgentePorFecha) {
            return Prioridad.ALTA;
        }

        // Regla 2: Ambos factores críticos
        if (tieneImpacto && esTipoCritico) {
            return Prioridad.ALTA;
        }

        // Regla 3: Al menos un factor crítico
        if (tieneImpacto || esTipoCritico) {
            return Prioridad.MEDIA;
        }

        // Regla 4: Sin factores críticos
        return Prioridad.BAJA;
    }

    /**
     * Genera una justificación textual legible para la prioridad calculada.
     */
    public String generarJustificacion(TipoSolicitud tipo,
                                        Boolean impactoAcademico,
                                        LocalDate fechaLimite,
                                        Prioridad prioridadCalculada) {
        StringBuilder sb = new StringBuilder();
        sb.append("Prioridad ").append(prioridadCalculada).append(" asignada por: ");

        if (esUrgentePorFecha(fechaLimite)) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaLimite);
            sb.append("fecha límite en ").append(dias).append(" días (urgencia temporal). ");
        }
        if (Boolean.TRUE.equals(impactoAcademico)) {
            sb.append("impacto académico directo. ");
        }
        if (TIPOS_CRITICOS.contains(tipo)) {
            sb.append("tipo de solicitud crítico (").append(tipo).append("). ");
        }
        if (prioridadCalculada == Prioridad.BAJA) {
            sb.append("sin factores de urgencia o impacto identificados.");
        }

        return sb.toString().trim();
    }

    private boolean esUrgentePorFecha(LocalDate fechaLimite) {
        if (fechaLimite == null) return false;
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaLimite);
        return diasRestantes >= 0 && diasRestantes <= DIAS_URGENCIA;
    }
}

package com.universidad.triage.entity;

/**
 * Estados posibles del ciclo de vida de una SolicitudAcademica.
 * RF-04: Las transiciones son unidireccionales y validadas por el dominio.
 */
public enum EstadoSolicitud {
    REGISTRADA,
    CLASIFICADA,
    EN_ATENCION,
    ATENDIDA,
    CERRADA
}

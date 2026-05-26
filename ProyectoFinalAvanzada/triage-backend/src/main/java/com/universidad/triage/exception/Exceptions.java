package com.universidad.triage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepciones personalizadas del dominio.
 * Cada excepción mapea directamente a un código HTTP.
 */
public class Exceptions {

    // ─── 404 Not Found ────────────────────────────────────────────────

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class RecursoNoEncontradoException extends RuntimeException {
        public RecursoNoEncontradoException(String mensaje) {
            super(mensaje);
        }

        public static RecursoNoEncontradoException solicitud(Long id) {
            return new RecursoNoEncontradoException("Solicitud no encontrada con ID: " + id);
        }

        public static RecursoNoEncontradoException usuario(Long id) {
            return new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
    }

    // ─── 409 Conflict ─────────────────────────────────────────────────

    /**
     * Se lanza cuando se intenta una transición de estado inválida.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class TransicionEstadoInvalidaException extends RuntimeException {
        public TransicionEstadoInvalidaException(String estadoActual, String accionIntentada) {
            super(String.format(
                    "No se puede realizar '%s' sobre una solicitud en estado '%s'",
                    accionIntentada, estadoActual));
        }
    }

    /**
     * Se lanza cuando se intenta modificar una solicitud cerrada.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class SolicitudCerradaException extends RuntimeException {
        public SolicitudCerradaException() {
            super("Una solicitud CERRADA no puede ser modificada. Es inmutable.");
        }
    }

    // ─── 400 Bad Request ──────────────────────────────────────────────

    /**
     * Se lanza cuando el responsable asignado no está activo.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ResponsableNoActivoException extends RuntimeException {
        public ResponsableNoActivoException(Long responsableId) {
            super("El usuario con ID " + responsableId +
                    " no está activo y no puede ser asignado como responsable.");
        }
    }

    /**
     * Se lanza al intentar registrar un email ya existente.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class EmailDuplicadoException extends RuntimeException {
        public EmailDuplicadoException(String email) {
            super("Ya existe un usuario registrado con el email: " + email);
        }
    }

    // ─── 403 Forbidden ────────────────────────────────────────────────

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AccesoNoAutorizadoException extends RuntimeException {
        public AccesoNoAutorizadoException(String mensaje) {
            super(mensaje);
        }
    }
}

package com.universidad.triage.exception;

import com.universidad.triage.dto.response.Responses.ErrorResponse;
import com.universidad.triage.exception.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTransicion(TransicionEstadoInvalidaException ex) {
        log.warn("Transición inválida: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "Transición de estado inválida", ex.getMessage());
    }

    @ExceptionHandler(SolicitudCerradaException.class)
    public ResponseEntity<ErrorResponse> handleCerrada(SolicitudCerradaException ex) {
        log.warn("Intento de modificar solicitud cerrada");
        return build(HttpStatus.CONFLICT, "Solicitud inmutable", ex.getMessage());
    }

    @ExceptionHandler(ResponsableNoActivoException.class)
    public ResponseEntity<ErrorResponse> handleResponsable(ResponsableNoActivoException ex) {
        log.warn("Responsable inactivo: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Responsable no válido", ex.getMessage());
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleEmailDup(EmailDuplicadoException ex) {
        log.warn("Email duplicado: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Email ya registrado", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String campos = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Validación fallida: {}", campos);
        return build(HttpStatus.BAD_REQUEST, "Error de validación", campos);
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccesoNoAutorizadoException ex) {
        log.warn("Acceso no autorizado: {}", ex.getMessage());
        return build(HttpStatus.FORBIDDEN, "Acceso no autorizado", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Error inesperado: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor",
                "Ocurrió un error inesperado. Contacte al administrador.");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String mensaje) {
        ErrorResponse body = ErrorResponse.builder()
                .status(status.value()).error(error).mensaje(mensaje)
                .timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(status).body(body);
    }
}

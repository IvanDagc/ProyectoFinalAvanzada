package com.universidad.triage.dto.response;

import com.universidad.triage.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Responses {

    // ── UsuarioResumen ────────────────────────────────────────────────

    public static class UsuarioResumen {
        private Long id;
        private String nombre;
        private String email;
        private RolUsuario rol;
        private boolean activo;

        public UsuarioResumen() {}
        private UsuarioResumen(Builder b) {
            this.id = b.id; this.nombre = b.nombre; this.email = b.email;
            this.rol = b.rol; this.activo = b.activo;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private String nombre; private String email;
            private RolUsuario rol; private boolean activo;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder nombre(String nombre) { this.nombre = nombre; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder rol(RolUsuario rol) { this.rol = rol; return this; }
            public Builder activo(boolean activo) { this.activo = activo; return this; }
            public UsuarioResumen build() { return new UsuarioResumen(this); }
        }
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }
        public RolUsuario getRol() { return rol; }
        public boolean isActivo() { return activo; }
        public void setId(Long id) { this.id = id; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setEmail(String email) { this.email = email; }
        public void setRol(RolUsuario rol) { this.rol = rol; }
        public void setActivo(boolean activo) { this.activo = activo; }
    }

    // ── SolicitudResponse ─────────────────────────────────────────────

    public static class SolicitudResponse {
        private Long id; private TipoSolicitud tipo; private String descripcion;
        private CanalOrigen canalOrigen; private LocalDateTime fechaRegistro;
        private EstadoSolicitud estado; private Prioridad prioridad;
        private String justificacionPrioridad; private String observacionCierre;
        private LocalDateTime fechaCierre; private UsuarioResumen solicitante;
        private UsuarioResumen responsable;

        public SolicitudResponse() {}
        private SolicitudResponse(Builder b) {
            this.id = b.id; this.tipo = b.tipo; this.descripcion = b.descripcion;
            this.canalOrigen = b.canalOrigen; this.fechaRegistro = b.fechaRegistro;
            this.estado = b.estado; this.prioridad = b.prioridad;
            this.justificacionPrioridad = b.justificacionPrioridad;
            this.observacionCierre = b.observacionCierre; this.fechaCierre = b.fechaCierre;
            this.solicitante = b.solicitante; this.responsable = b.responsable;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private TipoSolicitud tipo; private String descripcion;
            private CanalOrigen canalOrigen; private LocalDateTime fechaRegistro;
            private EstadoSolicitud estado; private Prioridad prioridad;
            private String justificacionPrioridad; private String observacionCierre;
            private LocalDateTime fechaCierre; private UsuarioResumen solicitante;
            private UsuarioResumen responsable;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder tipo(TipoSolicitud tipo) { this.tipo = tipo; return this; }
            public Builder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
            public Builder canalOrigen(CanalOrigen canalOrigen) { this.canalOrigen = canalOrigen; return this; }
            public Builder fechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; return this; }
            public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
            public Builder prioridad(Prioridad prioridad) { this.prioridad = prioridad; return this; }
            public Builder justificacionPrioridad(String j) { this.justificacionPrioridad = j; return this; }
            public Builder observacionCierre(String o) { this.observacionCierre = o; return this; }
            public Builder fechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; return this; }
            public Builder solicitante(UsuarioResumen solicitante) { this.solicitante = solicitante; return this; }
            public Builder responsable(UsuarioResumen responsable) { this.responsable = responsable; return this; }
            public SolicitudResponse build() { return new SolicitudResponse(this); }
        }
        public Long getId() { return id; }
        public TipoSolicitud getTipo() { return tipo; }
        public String getDescripcion() { return descripcion; }
        public CanalOrigen getCanalOrigen() { return canalOrigen; }
        public LocalDateTime getFechaRegistro() { return fechaRegistro; }
        public EstadoSolicitud getEstado() { return estado; }
        public Prioridad getPrioridad() { return prioridad; }
        public String getJustificacionPrioridad() { return justificacionPrioridad; }
        public String getObservacionCierre() { return observacionCierre; }
        public LocalDateTime getFechaCierre() { return fechaCierre; }
        public UsuarioResumen getSolicitante() { return solicitante; }
        public UsuarioResumen getResponsable() { return responsable; }
    }

    // ── SolicitudResumen ──────────────────────────────────────────────

    public static class SolicitudResumen {
        private Long id; private TipoSolicitud tipo; private EstadoSolicitud estado;
        private Prioridad prioridad; private LocalDateTime fechaRegistro;
        private String nombreSolicitante; private String nombreResponsable;

        public SolicitudResumen() {}
        private SolicitudResumen(Builder b) {
            this.id = b.id; this.tipo = b.tipo; this.estado = b.estado;
            this.prioridad = b.prioridad; this.fechaRegistro = b.fechaRegistro;
            this.nombreSolicitante = b.nombreSolicitante; this.nombreResponsable = b.nombreResponsable;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private TipoSolicitud tipo; private EstadoSolicitud estado;
            private Prioridad prioridad; private LocalDateTime fechaRegistro;
            private String nombreSolicitante; private String nombreResponsable;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder tipo(TipoSolicitud tipo) { this.tipo = tipo; return this; }
            public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
            public Builder prioridad(Prioridad prioridad) { this.prioridad = prioridad; return this; }
            public Builder fechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; return this; }
            public Builder nombreSolicitante(String nombreSolicitante) { this.nombreSolicitante = nombreSolicitante; return this; }
            public Builder nombreResponsable(String nombreResponsable) { this.nombreResponsable = nombreResponsable; return this; }
            public SolicitudResumen build() { return new SolicitudResumen(this); }
        }
        public Long getId() { return id; }
        public TipoSolicitud getTipo() { return tipo; }
        public EstadoSolicitud getEstado() { return estado; }
        public Prioridad getPrioridad() { return prioridad; }
        public LocalDateTime getFechaRegistro() { return fechaRegistro; }
        public String getNombreSolicitante() { return nombreSolicitante; }
        public String getNombreResponsable() { return nombreResponsable; }
    }

    // ── SolicitudClasificadaResponse ──────────────────────────────────

    public static class SolicitudClasificadaResponse {
        private Long id; private EstadoSolicitud estado; private TipoSolicitud tipo;
        private Prioridad prioridad; private String justificacionPrioridad;
        private Boolean impactoAcademico; private LocalDate fechaLimite;

        public SolicitudClasificadaResponse() {}
        private SolicitudClasificadaResponse(Builder b) {
            this.id = b.id; this.estado = b.estado; this.tipo = b.tipo;
            this.prioridad = b.prioridad; this.justificacionPrioridad = b.justificacionPrioridad;
            this.impactoAcademico = b.impactoAcademico; this.fechaLimite = b.fechaLimite;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private EstadoSolicitud estado; private TipoSolicitud tipo;
            private Prioridad prioridad; private String justificacionPrioridad;
            private Boolean impactoAcademico; private LocalDate fechaLimite;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
            public Builder tipo(TipoSolicitud tipo) { this.tipo = tipo; return this; }
            public Builder prioridad(Prioridad prioridad) { this.prioridad = prioridad; return this; }
            public Builder justificacionPrioridad(String j) { this.justificacionPrioridad = j; return this; }
            public Builder impactoAcademico(Boolean impactoAcademico) { this.impactoAcademico = impactoAcademico; return this; }
            public Builder fechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; return this; }
            public SolicitudClasificadaResponse build() { return new SolicitudClasificadaResponse(this); }
        }
        public Long getId() { return id; }
        public EstadoSolicitud getEstado() { return estado; }
        public TipoSolicitud getTipo() { return tipo; }
        public Prioridad getPrioridad() { return prioridad; }
        public String getJustificacionPrioridad() { return justificacionPrioridad; }
        public Boolean getImpactoAcademico() { return impactoAcademico; }
        public LocalDate getFechaLimite() { return fechaLimite; }
    }

    // ── SolicitudAsignadaResponse ─────────────────────────────────────

    public static class SolicitudAsignadaResponse {
        private Long id; private EstadoSolicitud estado; private UsuarioResumen responsable;

        public SolicitudAsignadaResponse() {}
        private SolicitudAsignadaResponse(Builder b) {
            this.id = b.id; this.estado = b.estado; this.responsable = b.responsable;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private EstadoSolicitud estado; private UsuarioResumen responsable;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
            public Builder responsable(UsuarioResumen responsable) { this.responsable = responsable; return this; }
            public SolicitudAsignadaResponse build() { return new SolicitudAsignadaResponse(this); }
        }
        public Long getId() { return id; }
        public EstadoSolicitud getEstado() { return estado; }
        public UsuarioResumen getResponsable() { return responsable; }
    }

    // ── SolicitudCerradaResponse ──────────────────────────────────────

    public static class SolicitudCerradaResponse {
        private Long id; private EstadoSolicitud estado;
        private String observacionCierre; private LocalDateTime fechaCierre;

        public SolicitudCerradaResponse() {}
        private SolicitudCerradaResponse(Builder b) {
            this.id = b.id; this.estado = b.estado;
            this.observacionCierre = b.observacionCierre; this.fechaCierre = b.fechaCierre;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private EstadoSolicitud estado;
            private String observacionCierre; private LocalDateTime fechaCierre;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
            public Builder observacionCierre(String o) { this.observacionCierre = o; return this; }
            public Builder fechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; return this; }
            public SolicitudCerradaResponse build() { return new SolicitudCerradaResponse(this); }
        }
        public Long getId() { return id; }
        public EstadoSolicitud getEstado() { return estado; }
        public String getObservacionCierre() { return observacionCierre; }
        public LocalDateTime getFechaCierre() { return fechaCierre; }
    }

    // ── HistorialEntradaResponse ──────────────────────────────────────

    public static class HistorialEntradaResponse {
        private Long id; private java.time.LocalDateTime fechaHora;
        private String accion; private String observaciones; private UsuarioResumen realizadoPor;

        public HistorialEntradaResponse() {}
        private HistorialEntradaResponse(Builder b) {
            this.id = b.id; this.fechaHora = b.fechaHora; this.accion = b.accion;
            this.observaciones = b.observaciones; this.realizadoPor = b.realizadoPor;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id; private java.time.LocalDateTime fechaHora;
            private String accion; private String observaciones; private UsuarioResumen realizadoPor;
            public Builder id(Long id) { this.id = id; return this; }
            public Builder fechaHora(java.time.LocalDateTime fechaHora) { this.fechaHora = fechaHora; return this; }
            public Builder accion(String accion) { this.accion = accion; return this; }
            public Builder observaciones(String observaciones) { this.observaciones = observaciones; return this; }
            public Builder realizadoPor(UsuarioResumen realizadoPor) { this.realizadoPor = realizadoPor; return this; }
            public HistorialEntradaResponse build() { return new HistorialEntradaResponse(this); }
        }
        public Long getId() { return id; }
        public java.time.LocalDateTime getFechaHora() { return fechaHora; }
        public String getAccion() { return accion; }
        public String getObservaciones() { return observaciones; }
        public UsuarioResumen getRealizadoPor() { return realizadoPor; }
    }

    // ── ErrorResponse ─────────────────────────────────────────────────

    public static class ErrorResponse {
        private int status; private String error; private String mensaje;
        private LocalDateTime timestamp;

        public ErrorResponse() {}
        private ErrorResponse(Builder b) {
            this.status = b.status; this.error = b.error;
            this.mensaje = b.mensaje; this.timestamp = b.timestamp;
        }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private int status; private String error; private String mensaje;
            private LocalDateTime timestamp;
            public Builder status(int status) { this.status = status; return this; }
            public Builder error(String error) { this.error = error; return this; }
            public Builder mensaje(String mensaje) { this.mensaje = mensaje; return this; }
            public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
            public ErrorResponse build() { return new ErrorResponse(this); }
        }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMensaje() { return mensaje; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}

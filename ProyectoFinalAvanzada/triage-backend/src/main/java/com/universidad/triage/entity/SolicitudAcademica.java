package com.universidad.triage.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes_academicas")
public class SolicitudAcademica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitud tipo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalOrigen canalOrigen;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.REGISTRADA;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Column(length = 500)
    private String justificacionPrioridad;

    private Boolean impactoAcademico;
    private LocalDate fechaLimite;

    @Column(length = 500)
    private String observacionCierre;

    private LocalDateTime fechaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("fechaHora DESC")
    private List<HistorialEntrada> historial = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
        if (estado == null) estado = EstadoSolicitud.REGISTRADA;
    }

    public SolicitudAcademica() {}

    private SolicitudAcademica(Builder b) {
        this.id = b.id;
        this.tipo = b.tipo;
        this.descripcion = b.descripcion;
        this.canalOrigen = b.canalOrigen;
        this.fechaRegistro = b.fechaRegistro;
        this.estado = b.estado != null ? b.estado : EstadoSolicitud.REGISTRADA;
        this.prioridad = b.prioridad;
        this.justificacionPrioridad = b.justificacionPrioridad;
        this.impactoAcademico = b.impactoAcademico;
        this.fechaLimite = b.fechaLimite;
        this.observacionCierre = b.observacionCierre;
        this.fechaCierre = b.fechaCierre;
        this.solicitante = b.solicitante;
        this.responsable = b.responsable;
        this.historial = b.historial != null ? b.historial : new ArrayList<>();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private TipoSolicitud tipo; private String descripcion;
        private CanalOrigen canalOrigen; private LocalDateTime fechaRegistro;
        private EstadoSolicitud estado; private Prioridad prioridad;
        private String justificacionPrioridad; private Boolean impactoAcademico;
        private LocalDate fechaLimite; private String observacionCierre;
        private LocalDateTime fechaCierre; private Usuario solicitante;
        private Usuario responsable; private List<HistorialEntrada> historial;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tipo(TipoSolicitud tipo) { this.tipo = tipo; return this; }
        public Builder descripcion(String descripcion) { this.descripcion = descripcion; return this; }
        public Builder canalOrigen(CanalOrigen canalOrigen) { this.canalOrigen = canalOrigen; return this; }
        public Builder fechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; return this; }
        public Builder estado(EstadoSolicitud estado) { this.estado = estado; return this; }
        public Builder prioridad(Prioridad prioridad) { this.prioridad = prioridad; return this; }
        public Builder justificacionPrioridad(String j) { this.justificacionPrioridad = j; return this; }
        public Builder impactoAcademico(Boolean impactoAcademico) { this.impactoAcademico = impactoAcademico; return this; }
        public Builder fechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; return this; }
        public Builder observacionCierre(String observacionCierre) { this.observacionCierre = observacionCierre; return this; }
        public Builder fechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; return this; }
        public Builder solicitante(Usuario solicitante) { this.solicitante = solicitante; return this; }
        public Builder responsable(Usuario responsable) { this.responsable = responsable; return this; }
        public Builder historial(List<HistorialEntrada> historial) { this.historial = historial; return this; }
        public SolicitudAcademica build() { return new SolicitudAcademica(this); }
    }

    public Long getId() { return id; }
    public TipoSolicitud getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public CanalOrigen getCanalOrigen() { return canalOrigen; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public EstadoSolicitud getEstado() { return estado; }
    public Prioridad getPrioridad() { return prioridad; }
    public String getJustificacionPrioridad() { return justificacionPrioridad; }
    public Boolean getImpactoAcademico() { return impactoAcademico; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public String getObservacionCierre() { return observacionCierre; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public Usuario getSolicitante() { return solicitante; }
    public Usuario getResponsable() { return responsable; }
    public List<HistorialEntrada> getHistorial() { return historial; }

    public void setId(Long id) { this.id = id; }
    public void setTipo(TipoSolicitud tipo) { this.tipo = tipo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCanalOrigen(CanalOrigen canalOrigen) { this.canalOrigen = canalOrigen; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }
    public void setJustificacionPrioridad(String justificacionPrioridad) { this.justificacionPrioridad = justificacionPrioridad; }
    public void setImpactoAcademico(Boolean impactoAcademico) { this.impactoAcademico = impactoAcademico; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public void setObservacionCierre(String observacionCierre) { this.observacionCierre = observacionCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public void setSolicitante(Usuario solicitante) { this.solicitante = solicitante; }
    public void setResponsable(Usuario responsable) { this.responsable = responsable; }
    public void setHistorial(List<HistorialEntrada> historial) { this.historial = historial; }
}

package com.universidad.triage.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_entradas")
public class HistorialEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, updatable = false, length = 500)
    private String accion;

    @Column(updatable = false, length = 500)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false, updatable = false)
    private SolicitudAcademica solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realizado_por_id", updatable = false)
    private Usuario realizadoPor;

    protected HistorialEntrada() {}

    private HistorialEntrada(Builder b) {
        this.fechaHora = b.fechaHora;
        this.accion = b.accion;
        this.observaciones = b.observaciones;
        this.solicitud = b.solicitud;
        this.realizadoPor = b.realizadoPor;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private LocalDateTime fechaHora; private String accion;
        private String observaciones; private SolicitudAcademica solicitud;
        private Usuario realizadoPor;

        public Builder fechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; return this; }
        public Builder accion(String accion) { this.accion = accion; return this; }
        public Builder observaciones(String observaciones) { this.observaciones = observaciones; return this; }
        public Builder solicitud(SolicitudAcademica solicitud) { this.solicitud = solicitud; return this; }
        public Builder realizadoPor(Usuario realizadoPor) { this.realizadoPor = realizadoPor; return this; }
        public HistorialEntrada build() { return new HistorialEntrada(this); }
    }

    public static HistorialEntrada crear(SolicitudAcademica solicitud, String accion,
                                          String observaciones, Usuario realizadoPor) {
        return HistorialEntrada.builder()
                .fechaHora(LocalDateTime.now())
                .accion(accion)
                .observaciones(observaciones)
                .solicitud(solicitud)
                .realizadoPor(realizadoPor)
                .build();
    }

    public Long getId() { return id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getAccion() { return accion; }
    public String getObservaciones() { return observaciones; }
    public SolicitudAcademica getSolicitud() { return solicitud; }
    public Usuario getRealizadoPor() { return realizadoPor; }
}

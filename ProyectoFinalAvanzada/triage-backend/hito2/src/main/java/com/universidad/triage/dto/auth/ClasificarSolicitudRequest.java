package com.universidad.triage.dto.request;

import com.universidad.triage.entity.TipoSolicitud;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ClasificarSolicitudRequest {

    @NotNull(message = "El tipo de solicitud es obligatorio")
    private TipoSolicitud tipo;

    @NotNull(message = "Debe indicar si tiene impacto académico")
    private Boolean impactoAcademico;

    private LocalDate fechaLimite;

    public ClasificarSolicitudRequest() {}

    public TipoSolicitud getTipo() { return tipo; }
    public Boolean getImpactoAcademico() { return impactoAcademico; }
    public LocalDate getFechaLimite() { return fechaLimite; }

    public void setTipo(TipoSolicitud tipo) { this.tipo = tipo; }
    public void setImpactoAcademico(Boolean impactoAcademico) { this.impactoAcademico = impactoAcademico; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
}

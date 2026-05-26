package com.universidad.triage.dto.request;

import com.universidad.triage.entity.CanalOrigen;
import com.universidad.triage.entity.TipoSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegistrarSolicitudRequest {

    @NotNull(message = "El tipo de solicitud es obligatorio")
    private TipoSolicitud tipo;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El canal de origen es obligatorio")
    private CanalOrigen canalOrigen;

    @NotNull(message = "El ID del solicitante es obligatorio")
    private Long solicitanteId;

    public RegistrarSolicitudRequest() {}

    public TipoSolicitud getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public CanalOrigen getCanalOrigen() { return canalOrigen; }
    public Long getSolicitanteId() { return solicitanteId; }

    public void setTipo(TipoSolicitud tipo) { this.tipo = tipo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCanalOrigen(CanalOrigen canalOrigen) { this.canalOrigen = canalOrigen; }
    public void setSolicitanteId(Long solicitanteId) { this.solicitanteId = solicitanteId; }
}

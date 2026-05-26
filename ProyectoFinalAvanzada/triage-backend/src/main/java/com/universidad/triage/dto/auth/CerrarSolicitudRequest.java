package com.universidad.triage.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CerrarSolicitudRequest {

    @NotBlank(message = "La observación de cierre es obligatoria")
    private String observacionCierre;

    public CerrarSolicitudRequest() {}

    public String getObservacionCierre() { return observacionCierre; }
    public void setObservacionCierre(String observacionCierre) { this.observacionCierre = observacionCierre; }
}

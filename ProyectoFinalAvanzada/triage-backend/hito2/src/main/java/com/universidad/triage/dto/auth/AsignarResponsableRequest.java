package com.universidad.triage.dto.request;

import jakarta.validation.constraints.NotNull;

public class AsignarResponsableRequest {

    @NotNull(message = "El ID del responsable es obligatorio")
    private Long responsableId;

    public AsignarResponsableRequest() {}

    public Long getResponsableId() { return responsableId; }
    public void setResponsableId(Long responsableId) { this.responsableId = responsableId; }
}

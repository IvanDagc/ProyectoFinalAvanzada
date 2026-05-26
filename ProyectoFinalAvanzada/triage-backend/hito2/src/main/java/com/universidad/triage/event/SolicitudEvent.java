package com.universidad.triage.event;

import com.universidad.triage.entity.SolicitudAcademica;
import com.universidad.triage.entity.Usuario;
import org.springframework.context.ApplicationEvent;

public class SolicitudEvent extends ApplicationEvent {

    private final SolicitudAcademica solicitud;
    private final String accion;
    private final String observaciones;
    private final Usuario realizadoPor;

    public SolicitudEvent(Object source, SolicitudAcademica solicitud, String accion,
                          String observaciones, Usuario realizadoPor) {
        super(source);
        this.solicitud = solicitud;
        this.accion = accion;
        this.observaciones = observaciones;
        this.realizadoPor = realizadoPor;
    }

    public SolicitudAcademica getSolicitud() { return solicitud; }
    public String getAccion() { return accion; }
    public String getObservaciones() { return observaciones; }
    public Usuario getRealizadoPor() { return realizadoPor; }
}

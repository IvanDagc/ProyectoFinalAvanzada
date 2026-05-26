package com.universidad.triage.listener;

import com.universidad.triage.entity.HistorialEntrada;
import com.universidad.triage.event.SolicitudEvent;
import com.universidad.triage.repository.HistorialEntradaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SolicitudEventListener {

    private static final Logger log = LoggerFactory.getLogger(SolicitudEventListener.class);

    private final HistorialEntradaRepository historialRepo;

    public SolicitudEventListener(HistorialEntradaRepository historialRepo) {
        this.historialRepo = historialRepo;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void onSolicitudCambiada(SolicitudEvent event) {
        log.debug("Registrando historial para solicitud {}: {}",
                event.getSolicitud().getId(), event.getAccion());
        HistorialEntrada entrada = HistorialEntrada.crear(
                event.getSolicitud(), event.getAccion(),
                event.getObservaciones(), event.getRealizadoPor());
        historialRepo.save(entrada);
        log.info("Historial registrado - Solicitud {}: {}",
                event.getSolicitud().getId(), event.getAccion());
    }
}

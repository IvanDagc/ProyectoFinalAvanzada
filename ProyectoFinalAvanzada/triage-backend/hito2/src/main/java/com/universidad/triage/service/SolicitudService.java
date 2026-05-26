package com.universidad.triage.service;

import com.universidad.triage.dto.request.*;
import com.universidad.triage.dto.response.Responses.*;
import com.universidad.triage.entity.*;
import com.universidad.triage.event.SolicitudEvent;
import com.universidad.triage.exception.Exceptions.*;
import com.universidad.triage.motor.MotorReglasPrioridad;
import com.universidad.triage.repository.SolicitudAcademicaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {

    private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);

    private final SolicitudAcademicaRepository solicitudRepo;
    private final UsuarioService usuarioService;
    private final MotorReglasPrioridad motorReglas;
    private final ApplicationEventPublisher eventPublisher;

    public SolicitudService(SolicitudAcademicaRepository solicitudRepo,
                             UsuarioService usuarioService,
                             MotorReglasPrioridad motorReglas,
                             ApplicationEventPublisher eventPublisher) {
        this.solicitudRepo = solicitudRepo;
        this.usuarioService = usuarioService;
        this.motorReglas = motorReglas;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public SolicitudResponse registrar(RegistrarSolicitudRequest request) {
        Usuario solicitante = usuarioService.buscarPorId(request.getSolicitanteId());
        SolicitudAcademica solicitud = SolicitudAcademica.builder()
                .tipo(request.getTipo())
                .descripcion(request.getDescripcion())
                .canalOrigen(request.getCanalOrigen())
                .solicitante(solicitante)
                .estado(EstadoSolicitud.REGISTRADA)
                .build();
        solicitud = solicitudRepo.save(solicitud);
        publicarEvento(solicitud, "Solicitud registrada vía " + solicitud.getCanalOrigen(),
                solicitud.getDescripcion(), solicitante);
        log.info("Solicitud {} registrada por usuario {}", solicitud.getId(), solicitante.getId());
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudClasificadaResponse clasificar(Long id, ClasificarSolicitudRequest request,
                                                    Long usuarioActuadorId) {
        SolicitudAcademica solicitud = buscarPorId(id);
        Usuario actuador = usuarioService.buscarPorId(usuarioActuadorId);
        validarEstado(solicitud, EstadoSolicitud.REGISTRADA, "clasificar");

        Prioridad prioridad = motorReglas.calcular(
                request.getTipo(), request.getImpactoAcademico(), request.getFechaLimite());
        String justificacion = motorReglas.generarJustificacion(
                request.getTipo(), request.getImpactoAcademico(), request.getFechaLimite(), prioridad);

        solicitud.setTipo(request.getTipo());
        solicitud.setPrioridad(prioridad);
        solicitud.setJustificacionPrioridad(justificacion);
        solicitud.setImpactoAcademico(request.getImpactoAcademico());
        solicitud.setFechaLimite(request.getFechaLimite());
        solicitud.setEstado(EstadoSolicitud.CLASIFICADA);
        solicitud = solicitudRepo.save(solicitud);

        publicarEvento(solicitud,
                String.format("Solicitud clasificada como %s con prioridad %s", request.getTipo(), prioridad),
                justificacion, actuador);
        log.info("Solicitud {} clasificada → tipo={}, prioridad={}", id, request.getTipo(), prioridad);
        return toClasificadaResponse(solicitud);
    }

    @Transactional
    public SolicitudAsignadaResponse asignarResponsable(Long id, AsignarResponsableRequest request,
                                                         Long usuarioActuadorId) {
        SolicitudAcademica solicitud = buscarPorId(id);
        Usuario actuador = usuarioService.buscarPorId(usuarioActuadorId);
        Usuario responsable = usuarioService.buscarPorId(request.getResponsableId());
        validarEstado(solicitud, EstadoSolicitud.CLASIFICADA, "asignar responsable");

        if (!responsable.isActivo()) {
            throw new ResponsableNoActivoException(responsable.getId());
        }
        solicitud.setResponsable(responsable);
        solicitud.setEstado(EstadoSolicitud.EN_ATENCION);
        solicitud = solicitudRepo.save(solicitud);

        publicarEvento(solicitud, "Responsable asignado: " + responsable.getNombre(),
                "Solicitud en atención por " + responsable.getNombre(), actuador);
        log.info("Solicitud {} asignada a responsable {}", id, responsable.getId());
        return toAsignadaResponse(solicitud);
    }

    @Transactional
    public SolicitudResponse marcarAtendida(Long id, Long usuarioActuadorId) {
        SolicitudAcademica solicitud = buscarPorId(id);
        Usuario actuador = usuarioService.buscarPorId(usuarioActuadorId);
        validarEstado(solicitud, EstadoSolicitud.EN_ATENCION, "marcar como atendida");
        solicitud.setEstado(EstadoSolicitud.ATENDIDA);
        solicitud = solicitudRepo.save(solicitud);
        publicarEvento(solicitud, "Solicitud marcada como ATENDIDA", null, actuador);
        log.info("Solicitud {} marcada como ATENDIDA por usuario {}", id, usuarioActuadorId);
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudCerradaResponse cerrar(Long id, CerrarSolicitudRequest request,
                                            Long usuarioActuadorId) {
        SolicitudAcademica solicitud = buscarPorId(id);
        Usuario actuador = usuarioService.buscarPorId(usuarioActuadorId);
        validarEstado(solicitud, EstadoSolicitud.ATENDIDA, "cerrar");
        solicitud.setObservacionCierre(request.getObservacionCierre());
        solicitud.setFechaCierre(java.time.LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.CERRADA);
        solicitud = solicitudRepo.save(solicitud);
        publicarEvento(solicitud, "Solicitud CERRADA formalmente", request.getObservacionCierre(), actuador);
        log.info("Solicitud {} CERRADA por usuario {}", id, usuarioActuadorId);
        return toCerradaResponse(solicitud);
    }

    @Transactional(readOnly = true)
    public Page<SolicitudResumen> buscarConFiltros(EstadoSolicitud estado, TipoSolicitud tipo,
                                                    Prioridad prioridad, Long responsableId,
                                                    Long solicitanteId, Pageable pageable) {
        return solicitudRepo.buscarConFiltros(estado, tipo, prioridad, responsableId, solicitanteId, pageable)
                .map(this::toResumen);
    }

    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorId(Long id) {
        return toResponse(buscarPorId(id));
    }

    private SolicitudAcademica buscarPorId(Long id) {
        return solicitudRepo.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.solicitud(id));
    }

    private void validarEstado(SolicitudAcademica solicitud, EstadoSolicitud estadoRequerido, String operacion) {
        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) throw new SolicitudCerradaException();
        if (solicitud.getEstado() != estadoRequerido)
            throw new TransicionEstadoInvalidaException(solicitud.getEstado().name(), operacion);
    }

    private void publicarEvento(SolicitudAcademica solicitud, String accion,
                                 String observaciones, Usuario realizadoPor) {
        eventPublisher.publishEvent(new SolicitudEvent(this, solicitud, accion, observaciones, realizadoPor));
    }

    private SolicitudResponse toResponse(SolicitudAcademica s) {
        return SolicitudResponse.builder()
                .id(s.getId()).tipo(s.getTipo()).descripcion(s.getDescripcion())
                .canalOrigen(s.getCanalOrigen()).fechaRegistro(s.getFechaRegistro())
                .estado(s.getEstado()).prioridad(s.getPrioridad())
                .justificacionPrioridad(s.getJustificacionPrioridad())
                .observacionCierre(s.getObservacionCierre()).fechaCierre(s.getFechaCierre())
                .solicitante(usuarioService.toResumen(s.getSolicitante()))
                .responsable(usuarioService.toResumen(s.getResponsable()))
                .build();
    }

    private SolicitudResumen toResumen(SolicitudAcademica s) {
        return SolicitudResumen.builder()
                .id(s.getId()).tipo(s.getTipo()).estado(s.getEstado())
                .prioridad(s.getPrioridad()).fechaRegistro(s.getFechaRegistro())
                .nombreSolicitante(s.getSolicitante() != null ? s.getSolicitante().getNombre() : null)
                .nombreResponsable(s.getResponsable() != null ? s.getResponsable().getNombre() : null)
                .build();
    }

    private SolicitudClasificadaResponse toClasificadaResponse(SolicitudAcademica s) {
        return SolicitudClasificadaResponse.builder()
                .id(s.getId()).estado(s.getEstado()).tipo(s.getTipo())
                .prioridad(s.getPrioridad()).justificacionPrioridad(s.getJustificacionPrioridad())
                .impactoAcademico(s.getImpactoAcademico()).fechaLimite(s.getFechaLimite())
                .build();
    }

    private SolicitudAsignadaResponse toAsignadaResponse(SolicitudAcademica s) {
        return SolicitudAsignadaResponse.builder()
                .id(s.getId()).estado(s.getEstado())
                .responsable(usuarioService.toResumen(s.getResponsable()))
                .build();
    }

    private SolicitudCerradaResponse toCerradaResponse(SolicitudAcademica s) {
        return SolicitudCerradaResponse.builder()
                .id(s.getId()).estado(s.getEstado())
                .observacionCierre(s.getObservacionCierre()).fechaCierre(s.getFechaCierre())
                .build();
    }
}

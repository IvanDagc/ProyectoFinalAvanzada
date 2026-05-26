package com.universidad.triage.controller;

import com.universidad.triage.dto.request.*;
import com.universidad.triage.dto.response.Responses.*;
import com.universidad.triage.entity.*;
import com.universidad.triage.service.HistorialService;
import com.universidad.triage.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final HistorialService historialService;

    public SolicitudController(SolicitudService solicitudService, HistorialService historialService) {
        this.solicitudService = solicitudService;
        this.historialService = historialService;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> registrar(@Valid @RequestBody RegistrarSolicitudRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<Page<SolicitudResumen>> buscar(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) TipoSolicitud tipo,
            @RequestParam(required = false) Prioridad prioridad,
            @RequestParam(required = false) Long responsableId,
            @RequestParam(required = false) Long solicitanteId,
            @PageableDefault(size = 20, sort = "fechaRegistro") Pageable pageable) {
        return ResponseEntity.ok(solicitudService.buscarConFiltros(estado, tipo, prioridad,
                responsableId, solicitanteId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/clasificar")
    public ResponseEntity<SolicitudClasificadaResponse> clasificar(
            @PathVariable Long id,
            @Valid @RequestBody ClasificarSolicitudRequest request,
            @RequestHeader("X-Usuario-Id") Long usuarioActuadorId) {
        return ResponseEntity.ok(solicitudService.clasificar(id, request, usuarioActuadorId));
    }

    @PatchMapping("/{id}/asignar-responsable")
    public ResponseEntity<SolicitudAsignadaResponse> asignarResponsable(
            @PathVariable Long id,
            @Valid @RequestBody AsignarResponsableRequest request,
            @RequestHeader("X-Usuario-Id") Long usuarioActuadorId) {
        return ResponseEntity.ok(solicitudService.asignarResponsable(id, request, usuarioActuadorId));
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitudResponse> marcarAtendida(
            @PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long usuarioActuadorId) {
        return ResponseEntity.ok(solicitudService.marcarAtendida(id, usuarioActuadorId));
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudCerradaResponse> cerrar(
            @PathVariable Long id,
            @Valid @RequestBody CerrarSolicitudRequest request,
            @RequestHeader("X-Usuario-Id") Long usuarioActuadorId) {
        return ResponseEntity.ok(solicitudService.cerrar(id, request, usuarioActuadorId));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEntradaResponse>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerHistorial(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clasificar")
    public ResponseEntity<?> clasificar() {
        return ResponseEntity.ok().build();
    }
}

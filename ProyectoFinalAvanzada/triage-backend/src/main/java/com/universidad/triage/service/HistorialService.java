package com.universidad.triage.service;

import com.universidad.triage.dto.response.Responses.HistorialEntradaResponse;
import com.universidad.triage.repository.HistorialEntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistorialService {

    private final HistorialEntradaRepository historialRepo;
    private final UsuarioService usuarioService;

    public HistorialService(HistorialEntradaRepository historialRepo, UsuarioService usuarioService) {
        this.historialRepo = historialRepo;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<HistorialEntradaResponse> obtenerHistorial(Long solicitudId) {
        return historialRepo
                .findBySolicitudIdOrderByFechaHoraDesc(solicitudId)
                .stream()
                .map(h -> HistorialEntradaResponse.builder()
                        .id(h.getId())
                        .fechaHora(h.getFechaHora())
                        .accion(h.getAccion())
                        .observaciones(h.getObservaciones())
                        .realizadoPor(usuarioService.toResumen(h.getRealizadoPor()))
                        .build())
                .toList();
    }
}

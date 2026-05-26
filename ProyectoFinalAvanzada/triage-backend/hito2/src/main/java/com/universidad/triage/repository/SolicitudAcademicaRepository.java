package com.universidad.triage.repository;

import com.universidad.triage.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * RF-07: Repositorio con soporte de filtros múltiples y paginación.
 */
@Repository
public interface SolicitudAcademicaRepository extends JpaRepository<SolicitudAcademica, Long> {

    /**
     * Consulta dinámica con todos los filtros opcionales.
     * Los parámetros null son ignorados en la cláusula WHERE.
     *
     * RF-07: Filtrar por estado, tipo, prioridad, responsable, solicitante.
     */
    @Query("""
            SELECT s FROM SolicitudAcademica s
            WHERE (:estado IS NULL OR s.estado = :estado)
              AND (:tipo IS NULL OR s.tipo = :tipo)
              AND (:prioridad IS NULL OR s.prioridad = :prioridad)
              AND (:responsableId IS NULL OR s.responsable.id = :responsableId)
              AND (:solicitanteId IS NULL OR s.solicitante.id = :solicitanteId)
            """)
    Page<SolicitudAcademica> buscarConFiltros(
            @Param("estado") EstadoSolicitud estado,
            @Param("tipo") TipoSolicitud tipo,
            @Param("prioridad") Prioridad prioridad,
            @Param("responsableId") Long responsableId,
            @Param("solicitanteId") Long solicitanteId,
            Pageable pageable
    );
}

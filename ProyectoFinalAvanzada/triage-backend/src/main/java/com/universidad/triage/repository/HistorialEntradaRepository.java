package com.universidad.triage.repository;

import com.universidad.triage.entity.HistorialEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEntradaRepository extends JpaRepository<HistorialEntrada, Long> {

    /**
     * RF-06: Historial completo de una solicitud, ordenado del más reciente al más antiguo.
     */
    List<HistorialEntrada> findBySolicitudIdOrderByFechaHoraDesc(Long solicitudId);
}

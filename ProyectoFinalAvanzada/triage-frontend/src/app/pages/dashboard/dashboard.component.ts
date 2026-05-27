import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../core/navbar/navbar.component';
import { SolicitudService } from '../../services/solicitud.service';
import {
  SolicitudResumen, EstadoSolicitud, TipoSolicitud, Prioridad,
  ESTADO_LABEL, TIPO_LABEL, PRIORIDAD_LABEL
} from '../../models/solicitud.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent, DatePipe],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  cargando = true;
  ultimasSolicitudes: SolicitudResumen[] = [];
  metricas = { total: 0, pendientes: 0, cerradas: 0, criticas: 0 };

  constructor(private solicitudService: SolicitudService) {}

  ngOnInit(): void {
    this.solicitudService.listarSolicitudes(undefined, undefined, undefined, 0, 50).subscribe({
      next: (page) => {
        const all = page.content;
        this.metricas = {
          total:      page.totalElements,
          pendientes: all.filter(s => ['REGISTRADA','CLASIFICADA','EN_ATENCION'].includes(s.estado)).length,
          cerradas:   all.filter(s => s.estado === 'CERRADA').length,
          criticas:   all.filter(s => s.prioridad === 'CRITICA').length,
        };
        this.ultimasSolicitudes = all.slice(0, 8);
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });
  }

  tipoLabel(t: TipoSolicitud)     { return TIPO_LABEL[t]      ?? t; }
  estadoLabel(e: EstadoSolicitud) { return ESTADO_LABEL[e]    ?? e; }
  prioridadLabel(p: Prioridad)    { return PRIORIDAD_LABEL[p] ?? p; }

  badgeEstado(e: EstadoSolicitud): string {
    return `badge-${e.toLowerCase().replace('_', '_')}`;
  }
  badgePrioridad(p: Prioridad): string {
    return `badge-${p.toLowerCase()}`;
  }
}
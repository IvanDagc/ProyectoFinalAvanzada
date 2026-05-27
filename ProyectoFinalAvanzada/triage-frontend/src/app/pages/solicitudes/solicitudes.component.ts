import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../core/navbar/navbar.component';
import { SolicitudService } from '../../services/solicitud.service';
import { AuthService } from '../../services/auth.service';
import {
  SolicitudResumen, SolicitudResponse, EstadoSolicitud,
  TipoSolicitud, Prioridad, ESTADO_LABEL, TIPO_LABEL, PRIORIDAD_LABEL
} from '../../models/solicitud.model';

@Component({
  selector: 'app-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, NavbarComponent, DatePipe],
  templateUrl: './solicitudes.component.html',
  styleUrls: ['./solicitudes.component.css']
})
export class SolicitudesComponent implements OnInit {

  solicitudes: SolicitudResumen[] = [];
  cargando = true;

  filtroEstado: EstadoSolicitud | '' = '';
  filtroTipo: TipoSolicitud | ''     = '';
  filtroPrioridad: Prioridad | ''    = '';

  paginacion = { pagina: 0, totalPaginas: 0, total: 0 };

  // Modales
  solicitudDetalle: SolicitudResponse | null = null;
  solicitudActiva:  SolicitudResumen  | null = null;
  historial: any[] = [];

  modalClasificar = false;
  modalCerrar     = false;
  accionCargando  = false;
  observacionCierre = '';

  formClasificar = {
    tipo: 'REGISTRO_ASIGNATURAS' as TipoSolicitud,
    prioridad: 'MEDIA' as Prioridad,
    justificacion: ''
  };

  private usuarioId = 0;

  constructor(
    private solicitudService: SolicitudService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const u = this.authService.obtenerUsuario();
    this.usuarioId = u?.id ?? 0;
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.solicitudService.listarSolicitudes(
      this.filtroEstado || undefined,
      this.filtroTipo   || undefined,
      this.filtroPrioridad || undefined,
      this.paginacion.pagina
    ).subscribe({
      next: (page) => {
        this.solicitudes = page.content;
        this.paginacion  = {
          pagina:       page.number,
          totalPaginas: page.totalPages,
          total:        page.totalElements
        };
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });
  }

  aplicarFiltros(): void { this.paginacion.pagina = 0; this.cargar(); }
  resetFiltros():    void { this.filtroEstado = ''; this.filtroTipo = ''; this.filtroPrioridad = ''; this.aplicarFiltros(); }
  cambiarPagina(p: number): void { this.paginacion.pagina = p; this.cargar(); }

  // ── Labels ──
  tipoLabel(t: TipoSolicitud)     { return TIPO_LABEL[t]      ?? t; }
  estadoLabel(e: EstadoSolicitud) { return ESTADO_LABEL[e]    ?? e; }
  prioridadLabel(p: Prioridad)    { return PRIORIDAD_LABEL[p] ?? p; }
  badgeEstado(e: EstadoSolicitud) { return `badge-${e.toLowerCase()}`; }
  badgePrioridad(p: Prioridad)    { return `badge-${p.toLowerCase()}`; }

  // ── Ver detalle ──
  verDetalle(s: SolicitudResumen): void {
    this.solicitudService.obtenerPorId(s.id).subscribe(det => {
      this.solicitudDetalle = det;
    });
    this.solicitudService.obtenerHistorial(s.id).subscribe(h => {
      this.historial = h;
    });
  }

  // ── Clasificar ──
  abrirClasificar(s: SolicitudResumen): void {
    this.solicitudActiva = s;
    this.formClasificar  = { tipo: s.tipo, prioridad: 'MEDIA', justificacion: '' };
    this.modalClasificar = true;
  }

  confirmarClasificar(): void {
    if (!this.solicitudActiva) return;
    this.accionCargando = true;
    this.solicitudService.clasificar(this.solicitudActiva.id, this.formClasificar, this.usuarioId)
      .subscribe({ next: () => { this.cerrarModal(); this.cargar(); }, error: () => { this.accionCargando = false; } });
  }

  // ── Atender ──
  marcarAtendida(s: SolicitudResumen): void {
    if (!confirm(`¿Marcar la solicitud #${s.id} como atendida?`)) return;
    this.solicitudService.marcarAtendida(s.id, this.usuarioId).subscribe(() => this.cargar());
  }

  // ── Cerrar ──
  abrirCerrar(s: SolicitudResumen): void {
    this.solicitudActiva  = s;
    this.observacionCierre = '';
    this.modalCerrar      = true;
  }

  confirmarCerrar(): void {
    if (!this.solicitudActiva || !this.observacionCierre.trim()) return;
    this.accionCargando = true;
    this.solicitudService.cerrar(this.solicitudActiva.id, this.observacionCierre, this.usuarioId)
      .subscribe({ next: () => { this.cerrarModal(); this.cargar(); }, error: () => { this.accionCargando = false; } });
  }

  cerrarModal(): void {
    this.solicitudDetalle = null;
    this.modalClasificar  = false;
    this.modalCerrar      = false;
    this.solicitudActiva  = null;
    this.accionCargando   = false;
    this.historial        = [];
  }
}
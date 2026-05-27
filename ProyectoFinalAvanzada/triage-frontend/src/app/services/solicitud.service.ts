import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  PageResponse, SolicitudResumen, SolicitudResponse,
  EstadoSolicitud, TipoSolicitud, Prioridad
} from '../models/solicitud.model';

@Injectable({ providedIn: 'root' })
export class SolicitudService {

  private base = `${environment.apiUrl}/api/v1/solicitudes`;

  constructor(private http: HttpClient) {}

  listarSolicitudes(
    estado?: EstadoSolicitud,
    tipo?: TipoSolicitud,
    prioridad?: Prioridad,
    page = 0,
    size = 20
  ): Observable<PageResponse<SolicitudResumen>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', 'fechaRegistro,desc');
    if (estado)    params = params.set('estado', estado);
    if (tipo)      params = params.set('tipo', tipo);
    if (prioridad) params = params.set('prioridad', prioridad);
    return this.http.get<PageResponse<SolicitudResumen>>(this.base, { params });
  }

  obtenerPorId(id: number): Observable<SolicitudResponse> {
    return this.http.get<SolicitudResponse>(`${this.base}/${id}`);
  }

  registrar(data: {
    tipo: TipoSolicitud;
    descripcion: string;
    canalOrigen: string;
    solicitanteId: number;
  }): Observable<SolicitudResponse> {
    return this.http.post<SolicitudResponse>(this.base, data);
  }

  clasificar(id: number, data: {
    tipo: TipoSolicitud;
    prioridad: Prioridad;
    justificacion: string;
  }, usuarioId: number): Observable<any> {
    return this.http.patch(`${this.base}/${id}/clasificar`, data, {
      headers: { 'X-Usuario-Id': String(usuarioId) }
    });
  }

  asignarResponsable(id: number, responsableId: number, usuarioId: number): Observable<any> {
    return this.http.patch(`${this.base}/${id}/asignar-responsable`,
      { responsableId },
      { headers: { 'X-Usuario-Id': String(usuarioId) } }
    );
  }

  marcarAtendida(id: number, usuarioId: number): Observable<any> {
    return this.http.patch(`${this.base}/${id}/atender`, {}, {
      headers: { 'X-Usuario-Id': String(usuarioId) }
    });
  }

  cerrar(id: number, observacion: string, usuarioId: number): Observable<any> {
    return this.http.patch(`${this.base}/${id}/cerrar`,
      { observacionCierre: observacion },
      { headers: { 'X-Usuario-Id': String(usuarioId) } }
    );
  }

  obtenerHistorial(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/${id}/historial`);
  }
}
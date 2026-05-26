import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SolicitudService {

  constructor(private http: HttpClient) {}

  listarSolicitudes() {
    return this.http.get(`${environment.apiUrl}/api/v1/solicitudes`);
  }
}
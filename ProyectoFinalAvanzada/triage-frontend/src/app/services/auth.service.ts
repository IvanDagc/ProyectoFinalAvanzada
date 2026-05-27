// triage-frontend/src/app/services/auth.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private http: HttpClient) {}

  login(data: { correo: string; password: string }) {
    return this.http.post<{ token: string }>(`${environment.apiUrl}/auth/login`, data);
  }

  guardarToken(token: string) {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
  }

  estaAutenticado(): boolean {
    return !!localStorage.getItem('token');
  }

  /** Decodifica el payload del JWT para obtener id, nombre y rol */
  obtenerUsuario(): { id: number; nombre: string; rol: string } | null {
    const token = this.obtenerToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        id:     payload.id     ?? payload.sub ?? 0,
        nombre: payload.nombre ?? payload.name ?? 'Usuario',
        rol:    payload.rol    ?? payload.role ?? 'USER',
      };
    } catch {
      return null;
    }
  }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  usuario: { id: number; nombre: string; rol: string } | null = null;
  iniciales = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();
    if (this.usuario?.nombre) {
      const parts = this.usuario.nombre.trim().split(' ');
      this.iniciales = parts.length >= 2
        ? parts[0][0] + parts[1][0]
        : parts[0].substring(0, 2);
      this.iniciales = this.iniciales.toUpperCase();
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
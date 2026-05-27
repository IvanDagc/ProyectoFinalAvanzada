import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  correo    = '';
  password  = '';
  cargando  = false;
  loginError = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.loginError = '';
    if (!this.correo || !this.password) {
      this.loginError = 'Por favor completa todos los campos.';
      return;
    }
    this.cargando = true;
    this.authService.login({ correo: this.correo, password: this.password }).subscribe({
      next: (resp) => {
        this.authService.guardarToken(resp.token);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.cargando = false;
        this.loginError = 'Credenciales inválidas. Verifica tu correo y contraseña.';
      }
    });
  }
}
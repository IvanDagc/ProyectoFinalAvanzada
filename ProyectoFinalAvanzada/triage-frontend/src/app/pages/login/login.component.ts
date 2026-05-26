import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
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

  correo = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {

    const body = {
      correo: this.correo,
      password: this.password
    };

    this.authService.login(body).subscribe({
      next: (resp: any) => {

        this.authService.guardarToken(resp.token);

        this.router.navigate(['/dashboard']);
      },
      error: () => {
        alert('Credenciales inválidas');
      }
    });
  }
}
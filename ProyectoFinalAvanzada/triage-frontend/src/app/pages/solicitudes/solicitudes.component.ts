import { Component, OnInit } from '@angular/core';
import { SolicitudService } from '../../services/solicitud.service';
import { CommonModule } from '@angular/common';

@Component({
  imports: [CommonModule],
  selector: 'app-solicitudes',
  standalone: true,
  templateUrl: './solicitudes.component.html'
  
})
export class SolicitudesComponent implements OnInit {

  solicitudes: any[] = [];

  constructor(private solicitudService: SolicitudService) {}

  ngOnInit(): void {

    this.solicitudService.listarSolicitudes().subscribe({
      next: (data: any) => {
        this.solicitudes = data;
      }
    });
  }
}
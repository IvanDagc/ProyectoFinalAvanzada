export type EstadoSolicitud =
  | 'REGISTRADA' | 'CLASIFICADA' | 'EN_ATENCION' | 'ATENDIDA' | 'CERRADA';

export type TipoSolicitud =
  | 'REGISTRO_ASIGNATURAS' | 'HOMOLOGACION' | 'CANCELACION_ASIGNATURAS'
  | 'SOLICITUD_CUPOS' | 'CONSULTA_ACADEMICA';

export type Prioridad = 'BAJA' | 'MEDIA' | 'ALTA' | 'CRITICA';

export type CanalOrigen = 'CSU' | 'CORREO' | 'SAC' | 'TELEFONICO' | 'PRESENCIAL';

export interface UsuarioResumen {
  id: number;
  nombre: string;
  email: string;
  rol: string;
  activo: boolean;
}

export interface SolicitudResumen {
  id: number;
  tipo: TipoSolicitud;
  estado: EstadoSolicitud;
  prioridad: Prioridad;
  fechaRegistro: string;
  nombreSolicitante: string;
  nombreResponsable?: string;
}

export interface SolicitudResponse extends SolicitudResumen {
  descripcion: string;
  canalOrigen: CanalOrigen;
  justificacionPrioridad?: string;
  observacionCierre?: string;
  fechaCierre?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export const ESTADO_LABEL: Record<EstadoSolicitud, string> = {
  REGISTRADA:   'Registrada',
  CLASIFICADA:  'Clasificada',
  EN_ATENCION:  'En atención',
  ATENDIDA:     'Atendida',
  CERRADA:      'Cerrada',
};

export const TIPO_LABEL: Record<TipoSolicitud, string> = {
  REGISTRO_ASIGNATURAS:  'Registro de asignaturas',
  HOMOLOGACION:          'Homologación',
  CANCELACION_ASIGNATURAS: 'Cancelación de asignaturas',
  SOLICITUD_CUPOS:       'Solicitud de cupos',
  CONSULTA_ACADEMICA:    'Consulta académica',
};

export const PRIORIDAD_LABEL: Record<Prioridad, string> = {
  BAJA:    'Baja',
  MEDIA:   'Media',
  ALTA:    'Alta',
  CRITICA: 'Crítica',
};
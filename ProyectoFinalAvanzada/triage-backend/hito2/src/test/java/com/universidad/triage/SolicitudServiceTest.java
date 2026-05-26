package com.universidad.triage;

import com.universidad.triage.dto.request.*;
import com.universidad.triage.entity.*;
import com.universidad.triage.event.SolicitudEvent;
import com.universidad.triage.exception.Exceptions.*;
import com.universidad.triage.motor.MotorReglasPrioridad;
import com.universidad.triage.repository.SolicitudAcademicaRepository;
import com.universidad.triage.service.SolicitudService;
import com.universidad.triage.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para SolicitudService.
 * Verifica la lógica de negocio y las transiciones de estado.
 */
@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock private SolicitudAcademicaRepository solicitudRepo;
    @Mock private UsuarioService usuarioService;
    @Mock private MotorReglasPrioridad motorReglas;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private SolicitudService solicitudService;

    private Usuario estudiante;
    private Usuario responsable;
    private SolicitudAcademica solicitudRegistrada;

    @BeforeEach
    void setUp() {
        estudiante = Usuario.builder()
                .id(1L).nombre("Estudiante Test")
                .rol(RolUsuario.ESTUDIANTE).activo(true).build();

        responsable = Usuario.builder()
                .id(2L).nombre("Responsable Test")
                .rol(RolUsuario.RESPONSABLE).activo(true).build();

        solicitudRegistrada = SolicitudAcademica.builder()
                .id(10L)
                .tipo(TipoSolicitud.HOMOLOGACION)
                .descripcion("Necesito homologar una materia")
                .canalOrigen(CanalOrigen.CORREO)
                .estado(EstadoSolicitud.REGISTRADA)
                .solicitante(estudiante)
                .build();
    }

    // ─── Registrar ────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar solicitud crea la entidad y publica evento")
    void registrar_debeCrearSolicitudYPublicarEvento() {
        RegistrarSolicitudRequest request = new RegistrarSolicitudRequest();
        request.setTipo(TipoSolicitud.HOMOLOGACION);
        request.setDescripcion("Solicitud de homologación");
        request.setCanalOrigen(CanalOrigen.CORREO);
        request.setSolicitanteId(1L);

        when(usuarioService.buscarPorId(1L)).thenReturn(estudiante);
        when(solicitudRepo.save(any())).thenReturn(solicitudRegistrada);
        when(usuarioService.toResumen(any())).thenReturn(null);

        solicitudService.registrar(request);

        verify(solicitudRepo).save(any(SolicitudAcademica.class));
        verify(eventPublisher).publishEvent(any(SolicitudEvent.class));
    }

    // ─── Clasificar ───────────────────────────────────────────────────

    @Test
    @DisplayName("Clasificar usa el motor de reglas, no acepta prioridad del request")
    void clasificar_debeUsarMotorReglas() {
        ClasificarSolicitudRequest request = new ClasificarSolicitudRequest();
        request.setTipo(TipoSolicitud.HOMOLOGACION);
        request.setImpactoAcademico(true);
        request.setFechaLimite(null);

        SolicitudAcademica guardada = SolicitudAcademica.builder()
                .id(10L).tipo(TipoSolicitud.HOMOLOGACION)
                .estado(EstadoSolicitud.CLASIFICADA).prioridad(Prioridad.ALTA)
                .justificacionPrioridad("Prioridad ALTA por motor").solicitante(estudiante)
                .impactoAcademico(true).build();

        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(solicitudRegistrada));
        when(usuarioService.buscarPorId(2L)).thenReturn(responsable);
        when(motorReglas.calcular(any(), anyBoolean(), any())).thenReturn(Prioridad.ALTA);
        when(motorReglas.generarJustificacion(any(), any(), any(), any()))
                .thenReturn("Prioridad ALTA por motor");
        when(solicitudRepo.save(any())).thenReturn(guardada);
        when(usuarioService.toResumen(any())).thenReturn(null);

        var resultado = solicitudService.clasificar(10L, request, 2L);

        // El motor fue invocado (no se usó prioridad del request)
        verify(motorReglas).calcular(TipoSolicitud.HOMOLOGACION, true, null);
        assertThat(resultado.getPrioridad()).isEqualTo(Prioridad.ALTA);
    }

    @Test
    @DisplayName("Clasificar sobre solicitud no en REGISTRADA lanza excepción 409")
    void clasificar_estadoInvalido_lanzaExcepcion() {
        solicitudRegistrada.setEstado(EstadoSolicitud.CLASIFICADA); // ya clasificada

        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(solicitudRegistrada));
        when(usuarioService.buscarPorId(anyLong())).thenReturn(responsable);

        ClasificarSolicitudRequest request = new ClasificarSolicitudRequest();
        request.setTipo(TipoSolicitud.HOMOLOGACION);
        request.setImpactoAcademico(false);

        assertThatThrownBy(() -> solicitudService.clasificar(10L, request, 2L))
                .isInstanceOf(TransicionEstadoInvalidaException.class);
    }

    // ─── Asignar responsable ──────────────────────────────────────────

    @Test
    @DisplayName("Asignar responsable inactivo lanza excepción 400")
    void asignarResponsable_inactivo_lanzaExcepcion() {
        responsable.setActivo(false); // responsable inactivo
        solicitudRegistrada.setEstado(EstadoSolicitud.CLASIFICADA);

        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(solicitudRegistrada));
        when(usuarioService.buscarPorId(1L)).thenReturn(estudiante);
        when(usuarioService.buscarPorId(2L)).thenReturn(responsable);

        AsignarResponsableRequest request = new AsignarResponsableRequest();
        request.setResponsableId(2L);

        assertThatThrownBy(() -> solicitudService.asignarResponsable(10L, request, 1L))
                .isInstanceOf(ResponsableNoActivoException.class);
    }

    // ─── Solicitud cerrada es inmutable ───────────────────────────────

    @Test
    @DisplayName("Cualquier operación sobre solicitud CERRADA lanza excepción 409")
    void solicitudCerrada_esInmutable() {
        solicitudRegistrada.setEstado(EstadoSolicitud.CERRADA);

        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(solicitudRegistrada));
        when(usuarioService.buscarPorId(anyLong())).thenReturn(responsable);

        CerrarSolicitudRequest request = new CerrarSolicitudRequest();
        request.setObservacionCierre("intentando cerrar de nuevo");

        assertThatThrownBy(() -> solicitudService.cerrar(10L, request, 2L))
                .isInstanceOf(SolicitudCerradaException.class);
    }

    // ─── Observer pattern ─────────────────────────────────────────────

    @Test
    @DisplayName("El evento publicado contiene la acción y la solicitud correctas")
    void evento_contieneInformacionCorrecta() {
        RegistrarSolicitudRequest request = new RegistrarSolicitudRequest();
        request.setTipo(TipoSolicitud.SOLICITUD_CUPO);
        request.setDescripcion("Necesito un cupo");
        request.setCanalOrigen(CanalOrigen.PRESENCIAL);
        request.setSolicitanteId(1L);

        when(usuarioService.buscarPorId(1L)).thenReturn(estudiante);
        when(solicitudRepo.save(any())).thenReturn(solicitudRegistrada);
        when(usuarioService.toResumen(any())).thenReturn(null);

        solicitudService.registrar(request);

        ArgumentCaptor<SolicitudEvent> captor = ArgumentCaptor.forClass(SolicitudEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());

        SolicitudEvent evento = captor.getValue();
        assertThat(evento.getSolicitud()).isNotNull();
        assertThat(evento.getAccion()).contains("PRESENCIAL");
    }
}

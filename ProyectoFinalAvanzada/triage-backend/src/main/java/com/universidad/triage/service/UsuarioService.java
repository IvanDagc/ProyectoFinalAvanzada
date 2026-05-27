package com.universidad.triage.service;

import com.universidad.triage.dto.request.ActualizarUsuarioRequest;
import com.universidad.triage.dto.request.RegistrarUsuarioRequest;
import com.universidad.triage.dto.response.Responses.UsuarioResumen;
import com.universidad.triage.entity.Usuario;
import com.universidad.triage.exception.Exceptions.EmailDuplicadoException;
import com.universidad.triage.exception.Exceptions.RecursoNoEncontradoException;
import com.universidad.triage.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResumen registrar(RegistrarUsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.getEmail())) {
            throw new EmailDuplicadoException(request.getEmail());
        }
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(true)
                .build();
        usuario = usuarioRepository.save(usuario);
        log.info("Usuario registrado: {} ({})", usuario.getNombre(), usuario.getRol());
        return toResumen(usuario);
    }

    @Transactional
    public UsuarioResumen actualizar(Long id, ActualizarUsuarioRequest request) {
        Usuario usuario = buscarPorId(id);
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
            log.info("Usuario {} → activo={}", id, request.getActivo());
        }
        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(usuario.getCorreo())
                && usuarioRepository.existsByCorreo(request.getEmail())) {
                throw new EmailDuplicadoException(request.getEmail());
            }
            usuario.setCorreo(request.getEmail());
        }
        return toResumen(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResumen obtenerPorId(Long id) {
        return toResumen(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResumen> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResumen)
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.usuario(id));
    }

    public UsuarioResumen toResumen(Usuario u) {
        if (u == null) return null;
        return UsuarioResumen.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .email(u.getCorreo())
                .rol(u.getRol())
                .activo(u.isActivo())
                .build();
    }
}
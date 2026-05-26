package com.universidad.triage.controller;

import com.universidad.triage.dto.request.ActualizarUsuarioRequest;
import com.universidad.triage.dto.request.RegistrarUsuarioRequest;
import com.universidad.triage.dto.response.Responses.UsuarioResumen;
import com.universidad.triage.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResumen> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResumen>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResumen> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResumen> actualizar(@PathVariable Long id,
                                                      @RequestBody ActualizarUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }
}

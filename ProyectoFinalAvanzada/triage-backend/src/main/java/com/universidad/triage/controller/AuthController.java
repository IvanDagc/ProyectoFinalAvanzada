package com.universidad.triage.controller;

import com.universidad.triage.dto.auth.LoginRequest;
import com.universidad.triage.dto.auth.LoginResponse;
import com.universidad.triage.entity.Usuario;
import com.universidad.triage.repository.UsuarioRepository;
import com.universidad.triage.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean valido = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!valido) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getCorreo());

        return new LoginResponse(token);
    }
}

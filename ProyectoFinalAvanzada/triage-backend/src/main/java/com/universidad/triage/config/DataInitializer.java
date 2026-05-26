package com.universidad.triage.config;

import com.universidad.triage.entity.RolUsuario;
import com.universidad.triage.entity.Usuario;
import com.universidad.triage.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (usuarioRepository.count() > 0) {
                log.info("Datos ya inicializados, omitiendo carga.");
                return;
            }
            usuarioRepository.save(Usuario.builder()
                    .nombre("Administrador Sistema").correo("admin@uniq.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(RolUsuario.ADMINISTRADOR).activo(true).build());
            usuarioRepository.save(Usuario.builder()
                    .nombre("María González").correo("responsable@uniq.edu")
                    .password(passwordEncoder.encode("resp123"))
                    .rol(RolUsuario.RESPONSABLE).activo(true).build());
            usuarioRepository.save(Usuario.builder()
                    .nombre("Juan Pérez").correo("estudiante@uniq.edu")
                    .password(passwordEncoder.encode("est123"))
                    .rol(RolUsuario.ESTUDIANTE).activo(true).build());

            log.info("==============================================");
            log.info("Datos iniciales cargados:");
            log.info("  ADMIN       → id=1  | admin@uniq.edu / admin123");
            log.info("  RESPONSABLE → id=2  | responsable@uniq.edu / resp123");
            log.info("  ESTUDIANTE  → id=3  | estudiante@uniq.edu / est123");
            log.info("  H2 Console  → http://localhost:8080/h2-console");
            log.info("==============================================");
        };
    }
}

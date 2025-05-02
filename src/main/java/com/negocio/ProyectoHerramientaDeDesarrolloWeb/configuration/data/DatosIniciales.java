package com.negocio.ProyectoHerramientaDeDesarrolloWeb.configuration.data;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner administrador(UsuarioRepository usuarioRepository) {
        Usuario usuario = Usuario.builder()
                .email("root@root.root")
                .contraseña(passwordEncoder.encode("root12345678"))
                .roles(Rol.ADMINISTRADOR)
                .build();

        return args -> {
            // Verifica si ya hay datos para evitar duplicados
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(usuario);

                System.out.println("Datos iniciales cargados! " +
                        "\n usuario = root" +
                        "\n contraseña = root");
            }
        };
    }
}

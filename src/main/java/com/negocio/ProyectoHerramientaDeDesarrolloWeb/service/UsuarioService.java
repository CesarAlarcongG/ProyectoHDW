package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.TokenJwt;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    //Métdos de creación de un ususario
    public ResponseEntity<?> registrarUsuario(UsuarioDto registroDto, String rol) {
        if (validarExistenciaDeUsuario(registroDto.getDni(), registroDto.getCorreo()))
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);

        var usuario = mapearAUsuario(registroDto, rol);

        if (guardarUsuario(usuario) == null)
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.CREATED);

        TokenJwt token = generarToken(usuario);

        UsuarioDto respuesta = mapearRespuesta(registroDto, token);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }


    private boolean validarExistenciaDeUsuario(String dni, String email) {
        return usuarioRepository.findByDniAndEmail(dni, email).isPresent();
    }

    private Usuario mapearAUsuario(UsuarioDto registroDto, String rol) {
        return Usuario.builder()
                .dni(registroDto.getDni())
                .nombres(registroDto.getNombre())
                .apellidos(registroDto.getApellido())
                .email(registroDto.getCorreo())
                .contraseña(passwordEncoder.encode(registroDto.getContraseña()))
                .estado(true)
                .fechaCreacion(new Date())
                .roles(registrarRol(rol))
                .build();
    }

    private Rol registrarRol(String rol) {
        return Rol.valueOf(rol);
    }

    private Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    private TokenJwt generarToken(Usuario usuario) {
        return new TokenJwt(jwtService.getToken(usuario));
    }
    private UsuarioDto mapearRespuesta(UsuarioDto usuarioDto, TokenJwt token){
        return UsuarioDto.builder()
                .nombre(usuarioDto.getNombre())
                .apellido(usuarioDto.getApellido())
                .correo(usuarioDto.getCorreo())
                .token(token)
                .build();
    }
}


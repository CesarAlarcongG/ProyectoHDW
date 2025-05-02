package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CredencialesDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.TokenJwt;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    /// /////////////////////////////////
    ///
    /// Registro de ususarios
    ///
    /// ////////////////////////////////
    public ResponseEntity<?> registrarUsuario(UsuarioDto registroDto, String rol) {
        if (validarExistenciaDeUsuario(registroDto.getDni(), registroDto.getCorreo()))
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);

        var usuario = mapearAUsuario(registroDto, rol);

        if (guardarUsuario(usuario) == null)
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.NOT_FOUND);

        if(rol.equals("ALUMNO")){
            TokenJwt token = generarToken(usuario);
            UsuarioDto respuesta = mapearRespuesta(usuario, token);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Se creo al usuario", HttpStatus.CREATED);
    }

    /// /////////////////////////////////
    ///
    /// Login de usuarios
    ///
    /// ////////////////////////////////
    public ResponseEntity<?> login( CredencialesDto credencialesDto) {
        try {
            // 1. Autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credencialesDto.getEmail(),
                            credencialesDto.getContraseña()
                    )
            );

            // 2. Obtener usuario
            Usuario usuario = usuarioRepository.findByEmail(credencialesDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // 3. Generar token
            String token = jwtService.getToken(usuario);


            return ResponseEntity.ok(mapearRespuesta(usuario, new TokenJwt(token)));

        } catch (BadCredentialsException e) {
            // Credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));

        } catch (DisabledException e) {
            // Usuario deshabilitado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario deshabilitado"));

        } catch (LockedException e) {
            // Cuenta bloqueada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Cuenta bloqueada"));

        } catch (Exception e) {
            // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor: " + e.getMessage()));
        }
    }

    /// /////////////////////////////////
    ///
    /// ACtualización de usuarios
    ///
    /// ////////////////////////////////

    public ResponseEntity<String> actualizar(UsuarioDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + dto.getCorreo()));

        // Actualizamos solo los campos permitidos si no son nulos o vacíos
        if (dto.getDni() != null && !dto.getDni().isBlank()) {
            usuario.setDni(dto.getDni());
        }

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            usuario.setNombres(dto.getNombre());
        }

        if (dto.getApellido() != null && !dto.getApellido().isBlank()) {
            usuario.setApellidos(dto.getApellido());
        }

        if (dto.getContraseña() != null && !dto.getContraseña().isBlank()) {
            usuario.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        }

        if (dto.getRol() != null && !dto.getRol().isBlank()) {
            try {
                usuario.setRoles(Rol.valueOf(dto.getRol().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol inválido: " + dto.getRol());
            }
        }

        if (dto.getCursos() != null && !dto.getCursos().isEmpty()) {
            usuario.setCursosAlumno(dto.getCursos()); // cuidado si deseas agregar o reemplazar
        }

        System.out.println("Este es el objeto: " +usuario.toString());
        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Se actualizo al usuario", HttpStatus.OK);
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

    private UsuarioDto mapearRespuesta(Usuario usuario, TokenJwt token){
        return UsuarioDto.builder()
                .nombre(usuario.getNombres())
                .apellido(usuario.getApellidos())
                .correo(usuario.getEmail())
                .token(token)
                .dni(usuario.getDni())
                .rol(usuario.getRoles().toString())
                .build();
    }



}


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
    /**
    public ResponseEntity<?> actualizar(UsuarioDto usuarioDto){
        Usuario usuario = usuarioRepository.findByEmail(usuarioDto.getCorreo())
                .orElseThrow(() -> new RuntimeException("No se encontro al usuario"));






    }


    public Usuario actualizarObjetoUsuario(UsuarioDto usuarioDto) throws IllegalAccessException{
        Usuario usuario = usuarioRepository.findByEmail(usuarioDto.getCorreo())
                .orElseThrow(() -> new RuntimeException("No se encontro al usuario"));

        Field[] camposUsuario = usuarioDto.getClass().getDeclaredFields();
        Field[] camposDto = usuario.getClass().getDeclaredFields();

        for (Field campoDto : camposDto){
            campoDto.setAccessible(true);
            Object valor = campoDto.get(usuarioDto);

            if(valor != null){

            }
        }
    }
     */

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


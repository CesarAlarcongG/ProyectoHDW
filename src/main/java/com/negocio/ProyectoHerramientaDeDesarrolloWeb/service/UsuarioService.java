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

import java.util.Date;
import java.util.List;
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
    /// ACtualización de usuarios
    ///
    /// ////////////////////////////////

    public ResponseEntity<?> actualizar(UsuarioDto dto) {
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
            usuario.setCursosUsuario(dto.getCursos()); // cuidado si deseas agregar o reemplazar
        }

        System.out.println("Este es el objeto: " +usuario.toString());
        usuarioRepository.save(usuario);

        return new ResponseEntity<>(usuario,  HttpStatus.OK);
    }

    /// /////////////////////////////////
    ///
    /// Obtener información de  usuario
    ///
    /// ////////////////////////////////
    public ResponseEntity<?> obtenerPorId(long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id = "+id+" no encontrado"));
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
    public ResponseEntity<?> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay usuarios registrados");
        }

        return ResponseEntity.ok(usuarios);
    }
    /// /////////////////////////////////
    ///
    /// Eliminar usuario
    ///
    /// ////////////////////////////////

    public ResponseEntity<?> eliminarPorId(Long id) {
        try{
            usuarioRepository.deleteById(id);
        } catch (Exception e){
            System.out.println("Hay un problema al eliminar el ususario de id = "+id+ " El problema es: \n"+e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("El usuario fue eliminado con exito");
    }


    public boolean validarExistenciaDeUsuario(String dni, String email) {
        return usuarioRepository.findByDniAndEmail(dni, email).isPresent();
    }

    public Usuario mapearAUsuario(UsuarioDto registroDto, String rol) {
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

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public TokenJwt generarToken(Usuario usuario) {
        return new TokenJwt(jwtService.getToken(usuario));
    }

    public UsuarioDto mapearRespuesta(Usuario usuario, TokenJwt token){
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombres())
                .apellido(usuario.getApellidos())
                .correo(usuario.getEmail())
                .token(token)
                .dni(usuario.getDni())
                .rol(usuario.getRoles().toString())
                .build();
    }

    public Usuario obtenerUsuarioPoId(Long id){
        return usuarioRepository.findById(id).get();
    }

    public void autenticarCredenciales(CredencialesDto credencialesDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credencialesDto.getEmail(),
                        credencialesDto.getContraseña()
                )
        );
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }



}


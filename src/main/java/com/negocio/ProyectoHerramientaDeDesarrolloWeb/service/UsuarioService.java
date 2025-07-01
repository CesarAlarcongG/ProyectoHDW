package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CredencialesDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.TokenJwt;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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


    public Usuario obtenerPorId(long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id = "+id+" no encontrado"));
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();

    }

    public Usuario eliminarPorId(Long id) {
        usuarioRepository.deleteById(id);
        return usuarioRepository.findById(id).get();
    }

    public Usuario actualizarInformación(UsuarioDto dto) {
        Usuario usuario = Usuario.builder()
                .dni(dto.getDni())
                .nombres(dto.getNombre())
                .apellidos(dto.getApellido())
                .email(dto.getCorreo())
                .contraseña(passwordEncoder.encode(dto.getContraseña()))
                .build();
        if(dto.getRol() != null){
            usuario.setRoles(Rol.valueOf(dto.getRol()));
            usuario.setCursosDocente(dto.getCursosProfesor());
        }else{
            usuario.setCursosEstudiantes(dto.getCursosEstudiante());
        }
        return usuarioRepository.save(usuario);
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


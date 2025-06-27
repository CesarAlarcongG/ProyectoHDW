package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;


import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CredencialesDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.TokenJwt;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /// /////////////////////////////////
    ///
    /// Registro de ususarios
    ///
    /// ////////////////////////////////

    @PostMapping("/registro/alumno")
    public ResponseEntity<?> crearAlumno(@RequestBody UsuarioDto registroDto) {
        String rol = Rol.ALUMNO.toString();

        //1. Validar si el ususario ya existe en la BD con su correo
        if(usuarioService.validarExistenciaDeUsuario(registroDto.getDni(), registroDto.getCorreo())){
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);
        }

        //2. mapeamos al usuario en clase
        Usuario usuario = usuarioService.mapearAUsuario(registroDto, rol);

        //3. Guardamos en la BD
        if (usuarioService.guardarUsuario(usuario) == null) {
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.NOT_FOUND);
        }


        //4. Generamos token para el ALUMNO
        TokenJwt token = usuarioService.generarToken(usuario);
        UsuarioDto respuesta = usuarioService.mapearRespuesta(usuario, token);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);



    }

    @PostMapping("/registro")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDto registroDto) {
        String rol = registroDto.getRol();

        //1. Validar si el ususario ya existe en la BD con su correo
        if(usuarioService.validarExistenciaDeUsuario(registroDto.getDni(), registroDto.getCorreo())){
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);
        }

        //2. mapeamos al usuario en clase
        Usuario usuario = usuarioService.mapearAUsuario(registroDto, rol);

        //3. Guardamos en la BD
        if (usuarioService.guardarUsuario(usuario) == null) {
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuarioService.guardarUsuario(usuario), HttpStatus.CREATED);
    }

    /// /////////////////////////////////
    ///
    /// Login
    ///
    /// ////////////////////////////////
    ///
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesDto){
        try {
            usuarioService.autenticarCredenciales(credencialesDto);

            Usuario usuario = usuarioService.obtenerUsuarioPorEmail(credencialesDto.getEmail());

            TokenJwt token = usuarioService.generarToken(usuario);

            return ResponseEntity.ok(usuarioService.mapearRespuesta(usuario, token));

        }catch (Exception e){

            System.out.println("Error "+e);

        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hay un problema en el servidor");
    }

    /// /////////////////////////////////
    ///
    /// Actualizar información
    ///
    /// ////////////////////////////////

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioDto usuarioDto){

        Usuario usuario = usuarioService.actualizar(usuarioDto);
        if (usuario == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor");
        }else {
            return ResponseEntity.ok(usuario);
        }
    }

    /// /////////////////////////////////
    ///
    /// Obtener información de  usuario
    ///
    /// ////////////////////////////////

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable long id){
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodosLosUsuarios(){
        return usuarioService.obtenerTodos();
    }
    @PutMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        return usuarioService.eliminarPorId(id);
    }


}

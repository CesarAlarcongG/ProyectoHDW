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

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


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

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioDto usuarioDto){
        Usuario usuario = usuarioService.actualizarInformacion(usuarioDto);
        if (usuario == null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable long id){
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }else {
            return ResponseEntity.ok(usuario);
        }

    }

    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodosLosUsuarios(){
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        if (usuarios.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.eliminarPorId(id);

        if (usuario != null){
            return ResponseEntity.internalServerError().body("no se pudo eliminar el ususario");
        }
        return ResponseEntity.ok("el usuario fue leminado");
    }


}

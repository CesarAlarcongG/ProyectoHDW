package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;


import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CredencialesDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    //Alumnos
    @PostMapping("/registro/alumno")
    public ResponseEntity<?> crearAlumno(@RequestBody UsuarioDto registroDto) {
        return usuarioService.registrarUsuario(registroDto, "ALUMNO");

    }

    //Docentes y Admnistradores
    @PostMapping("/registro")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDto registroDto) {
        return usuarioService.registrarUsuario(registroDto, registroDto.getRol());
    }

    /// /////////////////////////////////
    ///
    /// Login
    ///
    /// ////////////////////////////////

    //Alumnos y profesores
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesDto){
        return usuarioService.login(credencialesDto);
    }

    /// /////////////////////////////////
    ///
    /// Actualizar información
    ///
    /// ////////////////////////////////


    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioDto usuarioDto){
        return usuarioService.actualizar(usuarioDto);
    }


/**

    //Elimina
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable int id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setEstado(false);
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    */
}

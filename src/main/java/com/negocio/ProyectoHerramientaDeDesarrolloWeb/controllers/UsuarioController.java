package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controllers;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.models.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Crea
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getDni() == null || usuario.getDni().length() != 8) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setFecha_creacion(new Date());
        usuario.setEstado(true);
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    //Obtiene
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    //Por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Actualiza
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable int id,
            @RequestBody Usuario usuarioActualizado) {

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setCodigo_utp(usuarioActualizado.getCodigo_utp());
                    usuario.setDni(usuarioActualizado.getDni());
                    usuario.setNombres(usuarioActualizado.getNombres());
                    usuario.setApellidos(usuarioActualizado.getApellidos());
                    usuario.setEmail(usuarioActualizado.getEmail());
                    usuario.setEstado(usuarioActualizado.isEstado());
                    Usuario usuarioGuardado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(usuarioGuardado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
}
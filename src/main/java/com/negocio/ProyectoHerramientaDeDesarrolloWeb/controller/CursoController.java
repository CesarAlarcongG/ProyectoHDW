package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CursoDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /// /////////////////////////////////
    ///
    /// Registro de cursos
    ///
    /// ////////////////////////////////

    //Alumnos
    @PostMapping("/registro")
    public ResponseEntity<?> registrarCurso(@RequestBody CursoDto cursoDto) {
        return cursoService.registrarCurso(cursoDto);

    }

    /// /////////////////////////////////
    ///
    /// Obtener cursos
    ///
    /// ////////////////////////////////
    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id){
        return cursoService.obtenerPorId(id);
    }
    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodos(){
        return cursoService.obtenerTodos();
    }

    /// /////////////////////////////////
    ///
    /// Eliminar cursos
    ///
    /// ////////////////////////////////

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<?> obtenerTodos(@PathVariable int id){
        return cursoService.eliminarPorId(id);
    }
}

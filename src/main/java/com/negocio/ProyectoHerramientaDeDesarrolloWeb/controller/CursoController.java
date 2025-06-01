package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CursoDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.UsuarioDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

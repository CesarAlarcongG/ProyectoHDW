package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.RecursosService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recurso")
public class RecursoController {
    @Autowired
    private RecursosService recursosService;

    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodosLosRecursos(){
        return recursosService.obtenerTodosLosCursos();
    }

}

package com.negocio.ProyectoHerramientaDeDesarrolloWeb.controller;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.RecursosDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.service.RecursosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recurso")
public class RecursoController {
    @Autowired
    private RecursosService recursosService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearRecurso( @RequestPart("file") MultipartFile archivo, @RequestPart("json") RecursosDto recursosDto){
        recursosDto.setArchivo(archivo);
        recursosDto.setUbicación("uploads/"+archivo.getOriginalFilename());
        return recursosService.registarRecurso(recursosDto);
    }

    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTodosLosRecursos(){
        return recursosService.obtenerTodosLosCursos();
    }

}

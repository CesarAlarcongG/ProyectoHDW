package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Recursos;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.RecursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursosService {
    @Autowired
    private RecursosRepository recursosRepository;

    public ResponseEntity<?> obtenerTodosLosCursos() {
        List<Recursos> recursos = recursosRepository.findAll();
        if(recursos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontro ningun recurso");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(recursos);


    }
}

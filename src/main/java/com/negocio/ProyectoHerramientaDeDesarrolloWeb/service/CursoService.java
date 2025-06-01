package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CursoDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public ResponseEntity<?> registrarCurso(CursoDto cursoDto) {
        try {
            Curso cursoMapeado = mapearACurso(cursoDto);
            Curso curso = cursoRepository.save(cursoMapeado);
            return ResponseEntity.status(HttpStatus.CREATED).body(curso);
        } catch (Exception e){
            System.out.println("Hay un problema: "+e);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Hay un problema en el ervidor, no se puede almacenar el curso");

    }



    /// ////////////////////////////////////////////////////////////////////////
    public Curso mapearACurso (CursoDto cursoDto){
        return Curso.builder()
                .nombre(cursoDto.getNombre())
                .build();
    }
}

package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.CursoDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ResponseEntity<?> obtenerPorId(int id) {
        try {
            Optional<Curso> curso = cursoRepository.findById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(curso);
        }catch (Exception e){
            System.out.println("No se puede obtener al usuario en la BD. El problema es: \n"+e);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("No se pudo encontrar en la base de datos el curso");
    }

    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Curso> cursos = cursoRepository.findAll();
            return ResponseEntity.status(HttpStatus.FOUND).body(cursos);
        }catch (Exception e){
            System.out.println("No se puede obtener a los usuarios en la BD. El problema es: \n"+e);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("No se pudo encontrar en la base de datos los curso");
    }



    /// ////////////////////////////////////////////////////////////////////////
    public Curso mapearACurso (CursoDto cursoDto){
        return Curso.builder()
                .nombre(cursoDto.getNombre())
                .build();
    }


}

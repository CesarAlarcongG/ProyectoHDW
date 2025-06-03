package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface CursoRepository extends JpaRepository<Curso, Long> {
}

package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Recursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursosRepository extends JpaRepository<Recursos, Integer> {
}

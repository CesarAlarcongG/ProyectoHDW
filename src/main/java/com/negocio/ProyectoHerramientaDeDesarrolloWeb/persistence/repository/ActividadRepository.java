package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}

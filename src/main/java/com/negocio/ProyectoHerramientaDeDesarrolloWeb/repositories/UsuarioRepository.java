package com.negocio.ProyectoHerramientaDeDesarrolloWeb.repositories;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}

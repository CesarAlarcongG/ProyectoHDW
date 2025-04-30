package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository;


import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByDniAndEmail(String dni, String email);
    Optional<Usuario> findByEmail(String email);
}


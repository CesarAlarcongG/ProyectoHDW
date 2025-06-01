package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "cursosUsuario")
    private List<Usuario> usuarios;

    @ManyToOne
    @JoinColumn(name = "usuarioInteresado")
    private Usuario usuarioInteresado;
}

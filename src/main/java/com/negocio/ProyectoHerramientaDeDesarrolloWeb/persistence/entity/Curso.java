package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "curso")
    @JsonManagedReference(value = "curso_recursos")
    private List<Recursos> recursos = new ArrayList<>();
}

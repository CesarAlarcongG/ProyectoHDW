package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

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

    @ManyToMany(mappedBy = "cursosDocente")
    private List<Usuario> docentes;

    @ManyToMany(mappedBy = "cursosDocente")
    private List<Usuario> estudiantes;

    @OneToMany(mappedBy = "curso")
    @JsonManagedReference(value = "curso_recursos")
    private List<Recursos> recursos = new ArrayList<>();
}

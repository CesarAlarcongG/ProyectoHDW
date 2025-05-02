package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "cursosAlumno")
    private List<Usuario> alumnos;

    @ManyToOne
    @JoinColumn(name = "id_docente")
    private Usuario docente;
}

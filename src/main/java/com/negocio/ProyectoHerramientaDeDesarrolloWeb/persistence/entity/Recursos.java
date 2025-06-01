package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private String descripcion;
    private String rutaArchivo;
    private Date fechaCreación;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Curso curso;

    @OneToMany(mappedBy = "recursos")
    private List<Actividad> actividades;


}

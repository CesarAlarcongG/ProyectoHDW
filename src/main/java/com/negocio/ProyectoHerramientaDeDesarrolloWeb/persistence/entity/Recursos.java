package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Recursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private String descripcion;
    private String ubicación;
    private Date fechaCreación;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Curso curso;


    @OneToMany(mappedBy = "recursos")
    @JsonManagedReference
    private List<Actividad> actividades = new ArrayList<>();


}

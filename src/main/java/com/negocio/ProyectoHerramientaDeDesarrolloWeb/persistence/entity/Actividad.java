package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Permiso;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fecha;

    @Enumerated(EnumType.STRING)
    private Permiso actividad;

    @ManyToOne
    @JsonBackReference(value = "actividad_recursos")
    @JoinColumn(name = "id_recursos")
    private Recursos recursos;

    @ManyToOne
    @JsonBackReference(value = "actividad_usuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}

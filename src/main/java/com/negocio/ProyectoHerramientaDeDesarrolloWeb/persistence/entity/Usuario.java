package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Permiso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoUtp;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String contraseña;
    private boolean estado;
    private Date fechaCreacion;

    @Enumerated(EnumType.STRING)
    private Set<Rol> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "usuario_curso",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<Curso> cursosAlumno;

   @OneToMany(mappedBy = "docente")
    private List<Curso> cursosDocente;

    //Metodos para validar rol o permiso
    public boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }
    public boolean tienePermiso(Permiso permiso) {
        return roles.stream()
                .anyMatch(rol -> rol.getPermisos().contains(permiso));
    }

}

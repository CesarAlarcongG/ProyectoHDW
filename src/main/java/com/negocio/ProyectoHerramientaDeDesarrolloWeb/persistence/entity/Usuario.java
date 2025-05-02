package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {
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
    private Rol roles;

    @ManyToMany
    @JoinTable(
            name = "alumno_curso",
            joinColumns = @JoinColumn(name = "id_alumno"),
            inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<Curso> cursosAlumno;

   @OneToMany(mappedBy = "docente")
    private List<Curso> cursosDocente;


    //Configuración de Seguridad
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         return roles.getAuthorities();
    }

    @Override
    public String getPassword() {
        return contraseña;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Usuario (String email, String contraseña, Rol rol){

    }
}

package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@ToString
@Table(
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_nombre", columnList = "nombre"),
                @Index(name = "idx_dni_email", columnList = "dni, email")
        }
)
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String contraseña;

    @Enumerated(EnumType.STRING)
    private Rol roles;

    @ManyToMany
    @JoinTable(
            name = "curso_docente",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<Curso> cursosDocente;

    @ManyToMany
    @JoinTable(
            name = "curso_estudiante",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<Curso> cursosEstudiantes;

   @OneToMany(mappedBy = "usuario")
   @JsonManagedReference(value = "actividad_usuario")
   private List<Actividad> actividades;

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
}

package com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.TokenJwt;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private String contraseña;
    private String rol;
    private List<Curso> cursosEstudiante;
    private List<Curso> cursosProfesor;
    private TokenJwt token;

}


package com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecursosDto {

    private String titulo;
    private String descripcion;
    private String ubicación;
    private MultipartFile archivo;
    private Long idCurso;
    private Long idUsuario;
}

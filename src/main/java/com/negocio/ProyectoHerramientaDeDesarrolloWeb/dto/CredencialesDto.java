package com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialesDto {
    private String email;
    private String contraseña;

}

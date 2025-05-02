package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums;


import lombok.Getter;

public enum Permiso {
    /* Permisos de Usuarios */
    USUARIO_CREATE,  // ADMIN
    USUARIO_EDIT,    // ADMIN
    USUARIO_DELETE,  // ADMIN
    USUARIO_VIEW,    // PROF
    ALUMNO_EDIT,

    /* Permisos de Cursos */
    CURSO_CREATE,    // ADMIN
    CURSO_MANAGE,    // PROF
    CURSO_VIEW,      // ALUMNO
    CURSO_DELETE,    // ADMIN

    /* Permisos de Materiales */
    MATERIAL_CREATE, // PROF
    MATERIAL_EDIT,   // PROF
    MATERIAL_READ,   // ALUMNO
    MATERIAL_DELETE, // PROF

    /* Permisos de Sistema */
    SYSTEM_CONFIG   // ADMIN
   ;
    @Getter
    private String permisos;
}

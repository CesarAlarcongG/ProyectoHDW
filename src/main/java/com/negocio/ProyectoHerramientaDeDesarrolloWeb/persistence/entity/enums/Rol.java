package com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums;



import java.util.Set;

public enum Rol {
    ALUMNO(Set.of(
            Permiso.CURSO_VIEW,
            Permiso.MATERIAL_READ
    )),

    PROFESOR(Set.of(
            Permiso.CURSO_MANAGE,
            Permiso.MATERIAL_CREATE,
            Permiso.MATERIAL_EDIT

    )),

    ADMINISTRADOR(Set.of(
            Permiso.USUARIO_CREATE,
            Permiso.USUARIO_EDIT,
            Permiso.USUARIO_DELETE,
            Permiso.CURSO_CREATE,
            Permiso.CURSO_DELETE,
            Permiso.SYSTEM_CONFIG
    ));
    private final Set<Permiso> permisos;

    Rol(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

    public Set<Permiso> getPermisos() {
        return permisos;
    }
}

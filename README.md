# 📚 Repositorio de Recursos Educativos Digitales - UTP

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?logo=springboot)
![Java](https://img.shields.io/badge/Java-21-007396?logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📋 Descripción del Proyecto

Plataforma web para la gestión centralizada de recursos educativos en la Universidad Tecnológica del Perú que permite:

- **Docentes**: Crear, organizar y compartir materiales educativos
- **Estudiantes**: Buscar y descargar recursos de estudio
- **Administradores**: Gestionar usuarios y moderar contenido

## 🚀 Tecnologías Principales

| Componente       | Tecnología                                          |
|------------------|-----------------------------------------------------|
| **Backend**      | Spring Boot 3.4.5, Spring Security, Spring Data JPA |
| **Base de Datos**| MySQL 8.0           | 
| **Herramientas** | Lombok, Maven, JWT para autenticación               |

## Diagrama de la Base de Datos

![Diagrama ER de la aplicación](/uploads/Diagramas%20de%20Proyecto-Modelo%20Físico.jpg)

## 🧱 Estructura de Entidades

### `Usuario`
Representa a un administrador, docente o estudiante.

- `id`: Identificador único
- `dni`, `nombres`, `apellidos`, `email`, `contraseña`
- `roles`: Enum (ADMIN, DOCENTE, ESTUDIANTE)
- Relación con cursos:
    - `cursosDocente`: Cursos en los que es docente
    - `cursosEstudiantes`: Cursos en los que está inscrito
- Relación con actividades realizadas (ver recursos, subir, descargar, etc.)

### `Curso`
Curso académico.

- `id`, `nombre`
- `docentes`: Lista de usuarios con rol docente
- `estudiantes`: Lista de usuarios con rol estudiante
- `recursos`: Recursos educativos relacionados al curso

### `Recursos`
Material educativo (videos, PDFs, etc.).

- `id`, `titulo`, `descripcion`, `ubicación`, `fechaCreación`
- `curso`: Curso al que pertenece el recurso
- `actividades`: Lista de actividades relacionadas al recurso

### `Actividad`
Registro de interacciones de los usuarios con los recursos.

- `id`, `fecha`
- `actividad`: Enum (por ejemplo, VISUALIZACIÓN, DESCARGA)
- `usuario`: Usuario que realizó la acción
- `recursos`: Recurso implicado

---
## ⚙️ Tecnologías y Dependencias

- **Java 21**
- **Spring Boot 3.4.5**
    - spring-boot-starter-data-jpa
    - spring-boot-starter-security
    - spring-boot-starter-web
    - spring-boot-starter-test
- **MySQL**
- **Lombok**
- **JWT (Json Web Tokens)** para autenticación:
    - `jjwt-api`, `jjwt-impl`, `jjwt-jackson`

---

## 🔐 Seguridad

La autenticación está basada en **JWT**, y se implementa con `UserDetailsService` desde Spring Security. Cada usuario se autentica con su email y contraseña y recibe un token para acceder a los recursos según su rol.

---

## 🗃️ Base de Datos

Se utiliza **MySQL** como motor de base de datos. Las relaciones entre entidades están mapeadas con **JPA** usando anotaciones como `@ManyToMany`, `@OneToMany`, `@ManyToOne`, etc.

### Ejemplo de tablas generadas:

- `usuario`
- `curso`
- `recursos`
- `actividad`
- `curso_docente` (tabla intermedia)
- `curso_estudiante` (tabla intermedia)

---

## ▶️ Cómo Ejecutar el Proyecto

### 1. Clonar el Repositorio

```bash
git clone https://github.com/CesarAlarcongG/ProyectoHerramientaDeDesarrolloWeb.git
cd ProyectoHerramientaDeDesarrolloWeb
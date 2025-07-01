-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS ProyectoHDW;
USE ProyectoHDW;

-- Tabla de Usuarios
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(20) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    roles ENUM('ALUMNO', 'PROFESOR', 'ADMINISTRADOR') NOT NULL,
    UNIQUE KEY uk_email (email),
    INDEX idx_email (email),
    INDEX idx_nombre (nombres),
    INDEX idx_dni_email (dni, email)
);

-- Tabla de Cursos
CREATE TABLE curso (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de Recursos (actualizada con rutas correctas)
CREATE TABLE recursos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    ubicación VARCHAR(255) NOT NULL, -- Ahora es NOT NULL y con rutas relativas a uploads/
    fecha_creación DATE,
    id_curso BIGINT,
    FOREIGN KEY (id_curso) REFERENCES curso(id)
);

-- Tabla de Actividades
CREATE TABLE actividad (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fecha DATETIME NOT NULL,
    actividad ENUM(
        'USUARIO_CREATE', 'USUARIO_EDIT', 'USUARIO_DELETE', 'USUARIO_VIEW', 'ALUMNO_EDIT',
        'CURSO_CREATE', 'CURSO_MANAGE', 'CURSO_VIEW', 'CURSO_DELETE',
        'MATERIAL_CREATE', 'MATERIAL_EDIT', 'MATERIAL_READ', 'MATERIAL_DELETE',
        'SYSTEM_CONFIG'
    ) NOT NULL,
    id_recursos INT,
    id_usuario BIGINT NOT NULL,
    FOREIGN KEY (id_recursos) REFERENCES recursos(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Tablas de relación Many-to-Many

-- Curso-Docente
CREATE TABLE curso_docente (
    id_usuario BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_curso),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_curso) REFERENCES curso(id)
);

-- Curso-Estudiante
CREATE TABLE curso_estudiante (
    id_usuario BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_curso),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_curso) REFERENCES curso(id)
);

-- Inserción de datos de ejemplo con rutas correctas

-- Insertar 10 usuarios con diferentes roles (igual que antes)
INSERT INTO usuario (dni, nombres, apellidos, email, contraseña, roles) VALUES
('12345678', 'Juan', 'Pérez', 'juan.perez@example.com', '$2a$10$hashedpassword', 'ADMINISTRADOR'),
('23456789', 'María', 'Gómez', 'maria.gomez@example.com', '$2a$10$hashedpassword', 'ADMINISTRADOR'),
('34567890', 'Carlos', 'López', 'carlos.lopez@example.com', '$2a$10$hashedpassword', 'PROFESOR'),
('45678901', 'Ana', 'Martínez', 'ana.martinez@example.com', '$2a$10$hashedpassword', 'PROFESOR'),
('56789012', 'Pedro', 'Sánchez', 'pedro.sanchez@example.com', '$2a$10$hashedpassword', 'PROFESOR'),
('67890123', 'Lucía', 'Fernández', 'lucia.fernandez@example.com', '$2a$10$hashedpassword', 'ALUMNO'),
('78901234', 'Miguel', 'Rodríguez', 'miguel.rodriguez@example.com', '$2a$10$hashedpassword', 'ALUMNO'),
('89012345', 'Sofía', 'Díaz', 'sofia.diaz@example.com', '$2a$10$hashedpassword', 'ALUMNO'),
('90123456', 'David', 'Hernández', 'david.hernandez@example.com', '$2a$10$hashedpassword', 'ALUMNO'),
('01234567', 'Elena', 'Ruiz', 'elena.ruiz@example.com', '$2a$10$hashedpassword', 'ALUMNO');

-- Insertar cursos (igual que antes)
INSERT INTO curso (nombre) VALUES
('Matemáticas Avanzadas'),
('Programación Web'),
('Base de Datos'),
('Inteligencia Artificial'),
('Redes de Computadoras');

-- Asignar docentes a cursos (igual que antes)
INSERT INTO curso_docente (id_usuario, id_curso) VALUES
(3, 1), (3, 2), (4, 3), (5, 4), (5, 5);

-- Asignar estudiantes a cursos (igual que antes)
INSERT INTO curso_estudiante (id_usuario, id_curso) VALUES
(6, 1), (7, 1), (8, 1),
(6, 2), (7, 2), (9, 2), (10, 2),
(7, 3), (8, 3), (9, 3),
(6, 4), (8, 4), (10, 4),
(7, 5), (9, 5), (10, 5);

-- Insertar recursos con rutas correctas al directorio uploads/
INSERT INTO recursos (titulo, descripcion, ubicación, fecha_creación, id_curso) VALUES
('Apuntes de Álgebra', 'Documento con los conceptos básicos de álgebra', 'uploads/apuntes_algebra.pdf', '2023-01-15', 1),
('Guía de HTML5', 'Manual completo de HTML5 y CSS3', 'uploads/guia_html5.pdf', '2023-02-20', 2),
('Ejercicios SQL', 'Prácticas de consultas SQL', 'uploads/ejercicios_sql.pdf', '2023-03-10', 3),
('Introducción a ML', 'Conceptos básicos de Machine Learning', 'uploads/intro_ml.pptx', '2023-04-05', 4),
('Protocolos de Red', 'Descripción de protocolos TCP/IP', 'uploads/protocolos_red.docx', '2023-05-12', 5),
('Examen Parcial', 'Examen de mitad de curso', 'uploads/examen_parcial.pdf', '2023-06-01', 1),
('Proyecto Final', 'Requisitos del proyecto final', 'uploads/proyecto_final.pdf', '2023-06-15', 2),
('Archivo de Ejemplo', 'Documento de ejemplo para pruebas', 'uploads/archivoDeEjemplo.pdf', '2023-01-10', NULL),
('Proyecto Herramientas', 'Documentación del proyecto', 'uploads/Proyecto final - Herramientas de desarrollo.pdf', '2023-02-15', 2),
('Archivo Test', 'Archivo para pruebas técnicas', 'uploads/test.pdf', '2023-03-20', NULL);

-- Insertar actividades (igual que antes)
INSERT INTO actividad (fecha, actividad, id_recursos, id_usuario) VALUES
('2023-01-10 09:00:00', 'USUARIO_CREATE', NULL, 1),
('2023-01-11 10:30:00', 'CURSO_CREATE', NULL, 2),
('2023-02-01 08:15:00', 'MATERIAL_CREATE', 1, 3),
('2023-02-05 11:20:00', 'MATERIAL_CREATE', 2, 3),
('2023-03-12 14:00:00', 'MATERIAL_EDIT', 3, 4),
('2023-04-20 16:45:00', 'USUARIO_VIEW', NULL, 5),
('2023-02-15 10:00:00', 'MATERIAL_READ', 1, 6),
('2023-03-01 09:30:00', 'MATERIAL_READ', 2, 7),
('2023-04-10 11:15:00', 'CURSO_VIEW', NULL, 8),
('2023-05-20 15:00:00', 'MATERIAL_READ', 5, 9),
('2023-06-05 13:20:00', 'ALUMNO_EDIT', NULL, 10);
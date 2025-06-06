package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.RecursosDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Actividad;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Recursos;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Permiso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.RecursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecursosService {
    private final Path directorioBase = Paths.get("/home/zestian/Works/ProyectoHDW/uploads").toAbsolutePath().normalize();
    @Autowired
    private RecursosRepository recursosRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ActividadService actividadService;
    @Autowired
    private CursoService cursoService;

    public ResponseEntity<?> registarRecurso(RecursosDto recursosDto) {
        //1. Almacenar el archivo
        boolean fallo = almacenarArchivo(recursosDto.getArchivo());
        if (fallo) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo guardar el archivo en el sistema");
        }

        //2. Mapear y guardar
        Recursos recursos = mapearRecurso(recursosDto);
        Recursos recursoAlmacenado = recursosRepository.save(recursos);

        //3. Agregar actividad
        recursoAlmacenado = agregarActividad(recursoAlmacenado, recursosDto.getIdUsuario(), Permiso.MATERIAL_CREATE);
        if (recursoAlmacenado.getActividades().isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo agregar la actividad en la BD");
        }

        //4. Agregar curso
        recursoAlmacenado = agregarCurso(recursoAlmacenado, recursosDto.getIdCurso());

        //5. Agregar recurso a curso
        Curso curso = cursoService.agregarRecurso(recursoAlmacenado, recursosDto.getIdCurso());


        return ResponseEntity.ok(recursoAlmacenado);
    }

    public ResponseEntity<?> obtenerTodosLosCursos() {
        List<Recursos> recursos = recursosRepository.findAll();
        if (recursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontró ningún recurso");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(recursos);
    }

    public ResponseEntity<?> descargarArchivo(String nombreArchivo) {
        System.out.println("Solicitud para descargar: " + nombreArchivo);
        System.out.println("Directorio base: " + directorioBase.toString());

        Resource archivo = obtenerRecurso(nombreArchivo);

        if (archivo == null) {
            System.out.println("El archivo no se pudo cargar (null)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Archivo no encontrado: " + nombreArchivo);
        }
        if (!archivo.exists()) {
            System.out.println("El archivo no existe en la ruta especificada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Archivo no existe: " + nombreArchivo);
        }
        if (!archivo.isReadable()) {
            System.out.println("El archivo no es legible");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No se puede leer el archivo: " + nombreArchivo);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(archivo);
    }

    ///////////// MÉTODOS PRIVADOS /////////////

    private boolean almacenarArchivo(MultipartFile archivo) {
        try {
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null) return true;

            Path destino = directorioBase.resolve(nombreArchivo).normalize();
            Files.createDirectories(directorioBase);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return false;
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
            return true;
        }
    }

    private Resource obtenerRecurso(String nombreArchivo) {
        try {
            System.out.println("Nombre archivo recibido: " + nombreArchivo);
            Path archivoPath = directorioBase.resolve(nombreArchivo).normalize();
            System.out.println("Ruta completa buscada: " + archivoPath.toString());

            Resource recurso = new UrlResource(archivoPath.toUri());
            System.out.println("URI generada: " + archivoPath.toUri());

            if (recurso.exists()) {
                System.out.println("El recurso existe");
                if (recurso.isReadable()) {
                    System.out.println("El recurso es legible");
                    return recurso;
                } else {
                    System.out.println("El recurso existe pero no es legible");
                }
            } else {
                System.out.println("El recurso no existe en la ruta especificada");
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al cargar el recurso: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Recursos mapearRecurso(RecursosDto dto) {
        return Recursos.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .fechaCreación(new Date())
                .ubicación(dto.getArchivo().getOriginalFilename())
                .build();
    }

    private Recursos agregarActividad(Recursos recursos, Long idUsuario, Permiso permiso) {
        Usuario usuario = usuarioService.obtenerUsuarioPoId(idUsuario);
        Actividad actividad = Actividad.builder()
                .fecha(new Date())
                .actividad(permiso)
                .recursos(recursos)
                .usuario(usuario)
                .build();
        actividad = actividadService.almacenarActividad(actividad);

        List<Actividad> actividades = recursos.getActividades();
        if (actividades == null) {
            actividades = new ArrayList<>();
        }
        actividades.add(actividad);
        recursos.setActividades(actividades);

        return recursos;
    }

    private Recursos agregarCurso(Recursos recursos, Long idCurso) {
        Curso curso = cursoService.obtenerCursoPorId(idCurso);
        recursos.setCurso(curso);
        return recursos;
    }
}

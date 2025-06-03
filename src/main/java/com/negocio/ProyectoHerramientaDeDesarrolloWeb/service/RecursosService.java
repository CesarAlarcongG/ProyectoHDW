package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.dto.RecursosDto;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Actividad;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Curso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Recursos;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Usuario;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.enums.Permiso;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.RecursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecursosService {
    @Autowired
    private RecursosRepository recursosRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ActividadService actividadService;
    @Autowired
    private CursoService cursoService;

    public ResponseEntity<?> registarRecurso(RecursosDto recursosDto) {
        //1. Almacenamos el archivo en una carpeta local
        if(almacenarArchivo(recursosDto.getArchivo()) ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo guardar el archivo en el sistema");
        }
        //2. Mapear entidad
        Recursos recursos = mapearRecurso(recursosDto);
        //3. Almacenar en la BD
        Recursos recursoAlmacenado = recursosRepository.save(recursos);
        //4. Agregar actividad
        recursoAlmacenado = agregarActividad(recursoAlmacenado, recursosDto.getIdUsuario(), Permiso.MATERIAL_CREATE);
        if (recursoAlmacenado.getActividades().isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo agregar la actividad en la BD");
        }
        //5. Agregar Curso
        recursoAlmacenado = agregarCurso(recursoAlmacenado, recursosDto.getIdCurso());

        return  ResponseEntity.ok(recursoAlmacenado);
    }
    public ResponseEntity<?> obtenerTodosLosCursos() {
        List<Recursos> recursos = recursosRepository.findAll();
        if(recursos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontro ningun recurso");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(recursos);
    }

    /// //////////////////////////////////////////////////////////////////
    private boolean almacenarArchivo(MultipartFile archivo){
        try {
            String nombreArchivo = archivo.getOriginalFilename();
            String ruta = "uploads/" + nombreArchivo;
            Path path = Paths.get(ruta);
            Files.createDirectories(path.getParent());
            Files.copy(archivo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return false;
        } catch (IOException e){
            System.out.println("Hay un problema al guardar el archivo: "+e);
            return true;
        }
    }

    private Recursos mapearRecurso(RecursosDto recursosDto){
        return Recursos.builder()
                .titulo(recursosDto.getTitulo())
                .descripcion(recursosDto.getDescripcion())
                .fechaCreación(new Date())
                .ubicación(recursosDto.getUbicación())
                .build();
    }

    private Recursos agregarActividad(Recursos recursos, Long idUsuario, Permiso permiso){
        Usuario usuario = usuarioService.obtenerUsuarioPoId(idUsuario);
        Actividad actividad = Actividad.builder()
                .fecha(new Date())
                .actividad(Permiso.MATERIAL_CREATE)
                .recursos(recursos)
                .usuario(usuario)
                .actividad(permiso)
                .build();
        actividad = actividadService.almacenarActividad(actividad);
        List<Actividad> actividads = recursos.getActividades();
        if(actividads == null){
            actividads = new ArrayList<>();
        }
        actividads.add(actividad);

        recursos.setActividades(actividads);
        return recursos;
    }
    private Recursos agregarCurso(Recursos recursos, Long idCurso){
        Curso curso = cursoService.obtenerCursoPorId(idCurso);
        recursos.setCurso(curso);
        return recursos;
    }
}

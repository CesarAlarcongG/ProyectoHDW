package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Actividad;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {
    @Autowired
    private ActividadRepository actividadRepository;

    public Actividad almacenarActividad(Actividad actividad){
        return actividadRepository.save(actividad);
    }
}

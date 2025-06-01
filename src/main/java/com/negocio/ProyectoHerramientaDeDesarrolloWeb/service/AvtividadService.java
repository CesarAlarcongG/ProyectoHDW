package com.negocio.ProyectoHerramientaDeDesarrolloWeb.service;

import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.entity.Actividad;
import com.negocio.ProyectoHerramientaDeDesarrolloWeb.persistence.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvtividadService {
    @Autowired
    private ActividadRepository actividadRepository;

}

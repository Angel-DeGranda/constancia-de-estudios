package com.api.constancia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.constancia.models.SolicitudConstanciaModel;

@Repository
public interface SolicitudConstanciaRepository extends JpaRepository<SolicitudConstanciaModel, Long> {

    List<SolicitudConstanciaModel> findByEstudianteMatricula(String matricula);

    List<SolicitudConstanciaModel> findByEstatus(String estatus);

    // Buscar si el estudiante ya tiene una solicitud activa/en proceso
    Optional<SolicitudConstanciaModel> findTopByEstudianteMatriculaAndEstatusNot(String matricula, String estatusFinalizado);
}
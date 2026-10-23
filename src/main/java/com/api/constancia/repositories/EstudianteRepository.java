package com.api.constancia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.constancia.models.EstudianteModel;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteModel, String> {

    // verificar si el estudiante existe y está activo
    Optional<EstudianteModel> findByMatriculaAndActivoTrue(String matricula);

    boolean existsByMatriculaAndActivoTrue(String matricula);
}
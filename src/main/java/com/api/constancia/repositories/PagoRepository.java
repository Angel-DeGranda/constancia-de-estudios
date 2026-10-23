package com.api.constancia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.constancia.models.PagoModel;

@Repository
public interface PagoRepository extends JpaRepository<PagoModel, Long> {

    List<PagoModel> findByEstudianteMatricula(String matricula);

    // Verificar si existe un pago registrado con estatus 'PAGADO' para un alumno
    Optional<PagoModel> findTopByEstudianteMatriculaAndEstatusOrderByIdDesc(String matricula, String estatus);
}
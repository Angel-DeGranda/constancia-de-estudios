package com.api.constancia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.constancia.models.NotificacionModel;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionModel, Long> {

    List<NotificacionModel> findByEstudianteMatricula(String matricula);

    // Obtener notificaciones pendientes de envío (para procesamiento asíncrono o reintentos)
    List<NotificacionModel> findByEnviadoFalse();
}
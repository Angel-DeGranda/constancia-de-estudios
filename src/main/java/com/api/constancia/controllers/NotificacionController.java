package com.api.constancia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.constancia.repositories.NotificacionRepository;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    /**
     * Endpoint para consultar el historial de notificaciones enviadas a un estudiante.
     */
    @GetMapping("/estudiante/{matricula}")
    public ResponseEntity<?> consultarNotificacionesPorEstudiante(@PathVariable String matricula) {
        return ResponseEntity.ok(notificacionRepository.findByEstudianteMatricula(matricula));
    }
}
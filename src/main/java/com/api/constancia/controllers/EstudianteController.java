package com.api.constancia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.services.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    /**
     * Endpoint para verificar si un estudiante existe y se encuentra activo.
     */
    @GetMapping("/{matricula}/verificar")
    public ResponseEntity<?> verificarEstudiante(@PathVariable String matricula) {
        try {
            EstudianteModel estudiante = estudianteService.obtenerYValidarEstudianteActivo(matricula);
            return ResponseEntity.ok(estudiante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
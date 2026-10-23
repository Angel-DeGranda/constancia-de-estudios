package com.api.constancia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.repositories.EstudianteRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Transactional(readOnly = true)
    public EstudianteModel obtenerYValidarEstudianteActivo(String matricula) {
        if (matricula == null || matricula.trim().length() != 9) {
            throw new IllegalArgumentException("La matrícula debe contener exactamente 9 caracteres.");
        }

        return estudianteRepository.findByMatriculaAndActivoTrue(matricula)
                .orElseThrow(() -> new RuntimeException("El estudiante con matrícula " + matricula + " no fue encontrado o no se encuentra activo/inscrito."));
    }
}
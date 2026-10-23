package com.api.constancia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.models.PagoModel;
import com.api.constancia.services.EstudianteService;
import com.api.constancia.services.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private EstudianteService estudianteService;

    /**
     * Endpoint para realizar un cobro directo de trámite.
     */
    @PostMapping("/procesar")
    public ResponseEntity<?> procesarPago(
            @RequestParam String matricula,
            @RequestParam Double monto) {
        try {
            EstudianteModel estudiante = estudianteService.obtenerYValidarEstudianteActivo(matricula);
            PagoModel pago = pagoService.procesarPago(estudiante, monto);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
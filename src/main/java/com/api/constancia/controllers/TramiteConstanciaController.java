package com.api.constancia.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.constancia.models.SolicitudConstanciaModel;
import com.api.constancia.services.TramiteConstanciaService;

@RestController
@RequestMapping("/api/tramites/constancia")
public class TramiteConstanciaController {

    @Autowired
    private TramiteConstanciaService tramiteConstanciaService;

    /**
     * Endpoint para iniciar la solicitud de constancia de estudios.
     * Ejecuta en orden: Validación -> Pago -> PDF -> Envío por correo.
     */
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarConstancia(
            @RequestParam String matricula,
            @RequestParam Double monto) {
        
        try {
            SolicitudConstanciaModel solicitud = tramiteConstanciaService.procesarSolicitudConstancia(matricula, monto);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
            
        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación de datos (HTTP 400 Bad Request)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validación fallida");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            // Manejo de errores durante la transacción con Rollback activo (HTTP 500 Internal Server Error)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en el procesamiento de la constancia");
            errorResponse.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
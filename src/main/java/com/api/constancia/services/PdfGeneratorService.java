package com.api.constancia.services;

import org.springframework.stereotype.Service;

import com.api.constancia.models.EstudianteModel;

@Service
public class PdfGeneratorService {

    public String generarConstanciaPdf(EstudianteModel estudiante, Long folio) {
        try {
            // Lógica de simulación de generación de PDF
            if (estudiante.getNombre() == null || estudiante.getNombre().isEmpty()) {
                throw new RuntimeException("Error al generar el PDF: Nombre de estudiante no válido.");
            }

            String rutaPdf = "/documentos/constancias/constancia_" + folio + "_" + estudiante.getMatricula() + ".pdf";
            
            System.out.println("PDF generado exitosamente en: " + rutaPdf);
            return rutaPdf;

        } catch (Exception e) {
            throw new RuntimeException("Fallo en el servicio de generación de PDF: " + e.getMessage(), e);
        }
    }
}
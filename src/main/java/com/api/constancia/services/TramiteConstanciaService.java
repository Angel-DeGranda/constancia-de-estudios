package com.api.constancia.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.models.PagoModel;
import com.api.constancia.models.SolicitudConstanciaModel;
import com.api.constancia.repositories.SolicitudConstanciaRepository;

@Service
public class TramiteConstanciaService {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private SolicitudConstanciaRepository solicitudRepository;

    @Transactional
    public SolicitudConstanciaModel procesarSolicitudConstancia(String matricula, Double montoPago) {

        EstudianteModel estudiante = estudianteService.obtenerYValidarEstudianteActivo(matricula);

        PagoModel pago = pagoService.procesarPago(estudiante, montoPago);

        SolicitudConstanciaModel solicitud = new SolicitudConstanciaModel();
        solicitud.setEstudiante(estudiante);
        solicitud.setPago(pago);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstatus("PROCESANDO_DOCUMENTO");
        solicitud = solicitudRepository.save(solicitud);

        String rutaPdf = pdfGeneratorService.generarConstanciaPdf(estudiante, solicitud.getFolio());
        solicitud.setRutaPdf(rutaPdf);

        notificacionService.enviarCorreoConstancia(estudiante, rutaPdf);

        solicitud.setEstatus("ENVIADO_Y_COMPLETADO");
        return solicitudRepository.save(solicitud);
    }
}
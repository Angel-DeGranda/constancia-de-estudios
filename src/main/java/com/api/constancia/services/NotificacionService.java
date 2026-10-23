package com.api.constancia.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.models.NotificacionModel;
import com.api.constancia.repositories.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public NotificacionModel enviarCorreoConstancia(EstudianteModel estudiante, String rutaPdf) {
        if (estudiante.getCorreo() == null || !estudiante.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El estudiante no cuenta con una dirección de correo válida.");
        }

        NotificacionModel notificacion = new NotificacionModel();
        notificacion.setEstudiante(estudiante);
        notificacion.setAsunto("Constancia de Estudios Solicitada");
        notificacion.setMensaje("Estimado/a " + estudiante.getNombre() + ",\nAdjunto a este correo encontrará su constancia de estudios: " + rutaPdf);
        notificacion.setFechaEnvio(LocalDateTime.now());

        boolean envioExitoso = simularEnvioServidorSmtp(estudiante.getCorreo());

        if (!envioExitoso) {
            throw new RuntimeException("Error de red: No se pudo conectar con el servidor SMTP para enviar el correo a " + estudiante.getCorreo());
        }

        notificacion.setEnviado(true);
        return notificacionRepository.save(notificacion);
    }

    private boolean simularEnvioServidorSmtp(String correo) {
        // Retorna true simulando un envío exitoso
        return true; 
    }
}
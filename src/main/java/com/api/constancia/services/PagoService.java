package com.api.constancia.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.constancia.models.EstudianteModel;
import com.api.constancia.models.PagoModel;
import com.api.constancia.repositories.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    private static final Double COSTO_CONSTANCIA = 150.00;

    @Transactional
    public PagoModel procesarPago(EstudianteModel estudiante, Double monto) {
        if (monto == null || monto < COSTO_CONSTANCIA) {
            throw new IllegalArgumentException("El monto proporcionado ($" + monto + ") es insuficiente. El costo del trámite es de $" + COSTO_CONSTANCIA);
        }

        PagoModel pago = new PagoModel();
        pago.setEstudiante(estudiante);
        pago.setMonto(monto);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstatus("PAGADO");

        return pagoRepository.save(pago);
    }
}
package com.api.constancia.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pago")
public class PagoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false, length = 20)
    private String estatus; // Ejemplos: "PAGADO", "PENDIENTE", "CANCELADO"

    @ManyToOne
    @JoinColumn(name = "matricula_estudiante", nullable = false)
    private EstudianteModel estudiante;

    public PagoModel() {
    }

    public PagoModel(Double monto, LocalDateTime fechaPago, String estatus, EstudianteModel estudiante) {
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estatus = estatus;
        this.estudiante = estudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public EstudianteModel getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteModel estudiante) {
        this.estudiante = estudiante;
    }
}
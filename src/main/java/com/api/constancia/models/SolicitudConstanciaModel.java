package com.api.constancia.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_constancia")
public class SolicitudConstanciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folio;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(nullable = false, length = 30)
    private String estatus; 

    @Column(length = 255)
    private String rutaPdf;

    @ManyToOne
    @JoinColumn(name = "matricula_estudiante", nullable = false)
    private EstudianteModel estudiante;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private PagoModel pago;

    public SolicitudConstanciaModel() {
    }

    public SolicitudConstanciaModel(LocalDateTime fechaSolicitud, String estatus, String rutaPdf, EstudianteModel estudiante, PagoModel pago) {
        this.fechaSolicitud = fechaSolicitud;
        this.estatus = estatus;
        this.rutaPdf = rutaPdf;
        this.estudiante = estudiante;
        this.pago = pago;
    }

    public Long getFolio() {
        return folio;
    }

    public void setFolio(Long folio) {
        this.folio = folio;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getRutaPdf() {
        return rutaPdf;
    }

    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }

    public EstudianteModel getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteModel estudiante) {
        this.estudiante = estudiante;
    }

    public PagoModel getPago() {
        return pago;
    }

    public void setPago(PagoModel pago) {
        this.pago = pago;
    }
}
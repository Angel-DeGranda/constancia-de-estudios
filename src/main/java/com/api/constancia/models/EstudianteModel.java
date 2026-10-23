package com.api.constancia.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estudiante")
public class EstudianteModel {

    @Id
    @Column(length = 9)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false)
    private boolean activo;

    /*
     * @JsonIgnore
     * 
     * @OneToMany(mappedBy = "estudiante")
     * private List<PrestamoModel> prestamos = new ArrayList<>();
     */

    @JsonIgnore
    @OneToMany(mappedBy = "estudiante")
    private List<NotificacionModel> notificaciones = new ArrayList<>();

    public EstudianteModel() {
    }

    public EstudianteModel(String matricula, String nombre, String correo, boolean activo) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /* public List<PrestamoModel> getPrestamos() { 
        return prestamos; 
    } */

    public List<NotificacionModel> getNotificaciones() {
        return notificaciones;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

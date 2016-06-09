package com.busperapp.model;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cristhian.barros on 04/05/2016.
 */
public class Profile {

    private String _id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String direccionCasa;
    private Double latitudCasa;
    private Double longitudCasa;
    private String direccionTrabajo;
    private Double latitudTrabajo;
    private Double longitudTrabajo;
    private String celular;
    private String avatar;
    private String cotrasena;
    private String fechaCreacion;
    private String fechaActualizacion;
    private boolean estado;

    public Profile(String nombres, String apellidos, String correo) {

        this._id = UUID.randomUUID().toString();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;

    }

    public String get_id() {
        return _id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccionCasa() {
        return direccionCasa;
    }

    public void setDireccionCasa(String direccionCasa) {
        this.direccionCasa = direccionCasa;
    }

    public Double getLatitudCasa() {
        return latitudCasa;
    }

    public void setLatitudCasa(Double latitudCasa) {
        this.latitudCasa = latitudCasa;
    }

    public Double getLongitudCasa() {
        return longitudCasa;
    }

    public void setLongitudCasa(Double longitudCasa) {
        this.longitudCasa = longitudCasa;
    }

    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }

    public void setDireccionTrabajo(String direccionTrabajo) {
        this.direccionTrabajo = direccionTrabajo;
    }

    public Double getLatitudTrabajo() {
        return latitudTrabajo;
    }

    public void setLatitudTrabajo(Double latitudTrabajo) {
        this.latitudTrabajo = latitudTrabajo;
    }

    public Double getLongitudTrabajo() {
        return longitudTrabajo;
    }

    public void setLongitudTrabajo(Double longitudTrabajo) {
        this.longitudTrabajo = longitudTrabajo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCotrasena() {
        return cotrasena;
    }

    public void setCotrasena(String cotrasena) {
        this.cotrasena = cotrasena;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

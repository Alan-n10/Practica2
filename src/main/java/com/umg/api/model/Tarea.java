package com.umg.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo Tarea, según el enunciado:
 *  - id (entero, autoincremental, PK)
 *  - titulo (texto, obligatorio)
 *  - descripcion (texto)
 *  - estado (texto: "Pendiente", "En Progreso", "Completada")
 *  - responsable (texto, nombre de la persona asignada)
 *  - fecha_inicio date
 *  - fecha_fin date
 *  - fecha_limite (date, opcional)
 *
 * Las fechas se manejan como String "yyyy-MM-dd" para simplificar el
 * (de)serializado con Jackson sin agregar el módulo JavaTime.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tarea {

    private int id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String responsable;

    // ===== CAMBIAR EN EL PARCIAL (si hace falta) =====
    // El enunciado nombra los campos en snake_case (fecha_inicio, fecha_fin,
    // fecha_limite), que probablemente es tal cual como Sequelize los devuelve
    // en el JSON. @JsonProperty mapea eso al nombre camelCase de Java. Si al
    // probar ves que llegan null, confirma el nombre exacto del JSON real
    // (puede que el backend sí use camelCase) y ajusta aquí.
    @JsonProperty("fecha_inicio")
    private String fechaInicio;

    @JsonProperty("fecha_fin")
    private String fechaFin;

    @JsonProperty("fecha_limite")
    private String fechaLimite;

    public Tarea() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", responsable='" + responsable + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                ", fechaLimite='" + fechaLimite + '\'' +
                '}';
    }
}

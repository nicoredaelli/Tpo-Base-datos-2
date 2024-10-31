package org.example.entidades;



public class PuntoDeInteres {
    private String idPoi;
    private String nombre;
    private String descripcion;
    private String idZona;

    // Constructor
    public PuntoDeInteres(String idPoi, String nombre, String descripcion, String idZona) {
        this.idPoi = idPoi;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idZona = idZona;
    }

    // Getters y Setters
    public String getIdPoi() {
        return idPoi;
    }

    public void setIdPoi(String idPoi) {
        this.idPoi = idPoi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }
}

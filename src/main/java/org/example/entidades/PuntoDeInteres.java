package org.example.entidades;



public class PuntoDeInteres {
    private int idPoi;
    private String nombre;
    private String descripcion;
    private int zona;

    public int getIdPoi() {
        return idPoi;
    }

    public void setIdPoi(int idPoi) {
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

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    // Constructor
    public PuntoDeInteres(int idPoi, String nombre, String descripcion, int zona) {
        this.idPoi = idPoi;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.zona = zona;
    }
}

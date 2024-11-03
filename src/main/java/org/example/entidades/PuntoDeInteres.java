package org.example.entidades;


import org.bson.types.ObjectId;

public class PuntoDeInteres {
    private ObjectId objectIDPoi;
    private int idPoi;
    private String nombre;
    private String descripcion;
    private int zona;

    public void setObjectIDPoi(ObjectId objectIDPoi) {
        this.objectIDPoi = objectIDPoi;
    }

    public void setIdPoi(int idPoi) {
        this.idPoi = idPoi;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public ObjectId getObjectIDPoi() {
        return objectIDPoi;
    }

    public int getIdPoi() {
        return idPoi;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getZona() {
        return zona;
    }

    @Override
    public String toString() {
        return "PuntoDeInteres{" +
                "idPoi=" + idPoi +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", zona=" + zona +
                '}';
    }

    // Constructor
    public PuntoDeInteres(ObjectId objectIDPoi, int idPoi, String nombre, String descripcion, int zona) {
        this.objectIDPoi = objectIDPoi;
        this.idPoi = idPoi;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.zona = zona;
    }
}

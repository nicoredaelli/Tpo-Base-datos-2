package org.example.entidades;



public class Amenity {
    private int idAmenity;
    private String nombre;
    private String descripcion;

    public int getIdAmenity() {
        return idAmenity;
    }

    public void setIdAmenity(int idAmenity) {
        this.idAmenity = idAmenity;
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

    // Constructor
    public Amenity(int idAmenity, String nombre, String descripcion) {
        this.idAmenity = idAmenity;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}

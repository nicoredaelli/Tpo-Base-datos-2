package org.example.entidades;



public class Amenity {
    private String idAmenity;
    private String nombre;
    private String descripcion;

    // Constructor
    public Amenity(String idAmenity, String nombre, String descripcion) {
        this.idAmenity = idAmenity;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getIdAmenity() {
        return idAmenity;
    }

    public void setIdAmenity(String idAmenity) {
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
}

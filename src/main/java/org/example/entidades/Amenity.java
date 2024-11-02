package org.example.entidades;


import org.bson.types.ObjectId;

public class Amenity {
    private ObjectId ObjectIdAmenitie;
    private int idAmenity;
    private String nombre;
    private String descripcion;

    public int getIdAmenity() {
        return idAmenity;
    }

    public ObjectId getObjectIdAmenitie() {
        return ObjectIdAmenitie;
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

    @Override
    public String toString() {
        return "Amenity{" +
                "idAmenity=" + idAmenity +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    // Constructor
    public Amenity(ObjectId objectIdAmenitie, int idAmenity, String nombre, String descripcion) {
        this.ObjectIdAmenitie = objectIdAmenitie;
        this.idAmenity = idAmenity;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}

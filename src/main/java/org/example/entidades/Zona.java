package org.example.entidades;



public class Zona {
    private String idZona;
    private String nombre;
    private String provincia;
    private String pais;
    private String descripcion;

    // Constructor
    public Zona(String idZona, String nombre, String provincia, String pais, String descripcion) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.provincia = provincia;
        this.pais = pais;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package org.example.entidades;


public class Direccion {
    private String calle;
    private int numero;
    private String codigoPostal;
    private String provincia;
    private String pais;

    // Constructor
    public Direccion(String calle, int numero, String codigoPostal, String provincia, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.pais = pais;
    }

    // Getters y Setters
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
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
}

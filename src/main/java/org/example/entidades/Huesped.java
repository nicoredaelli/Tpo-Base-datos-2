package org.example.entidades;

import java.util.Map;

import org.bson.types.ObjectId;

public class Huesped {
    private ObjectId objectIdHuesped;
    private int idHuesped;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private Map<String, String> direccion;

    public int getIdHuesped() {
        return idHuesped;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, String> getDireccion() {
        return direccion;
    }

    public ObjectId getObjectIdHuesped() {
        return objectIdHuesped;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder direccionStr = new StringBuilder();
        if (direccion != null) {
            direccionStr.append("{");
            direccion.forEach((key, value) -> direccionStr.append(key).append(": ").append(value).append(", "));
            if (direccionStr.length() > 1) {
                direccionStr.setLength(direccionStr.length() - 2); // Elimina la última coma y espacio
            }
            direccionStr.append("}");
        } else {
            direccionStr.append("null");
        }

        return "Huesped{" +
                "idHuesped=" + idHuesped +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion=" + direccionStr +
                '}';
    }

    // Constructor
    public Huesped(ObjectId objectIdHuesped, int idHuesped, String nombre, String apellido, String telefono, String email, Map<String, String> direccion) {
        this.objectIdHuesped = objectIdHuesped;
        this.idHuesped = idHuesped;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }
}

package org.example.entidades;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

public class Hotel {
     private ObjectId idHotel;
    private String nombre;
    private String telefono;
    private String email;
    private Map<String, String> direccion;
    private List<Integer> habitaciones;
    private int zona;

    public ObjectId getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(ObjectId idHotel) {
        this.idHotel = idHotel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getDireccion() {
        return direccion;
    }

    public void setDireccion(Map<String, String> direccion) {
        this.direccion = direccion;
    }

    public List<Integer> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Integer> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    // Constructor
    public Hotel(ObjectId idHotel, String nombre, String telefono, String email, Map<String, String> direccion, List<Integer> habitaciones, int zona) {
        this.idHotel = idHotel;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.habitaciones = habitaciones;
        this.zona = zona;
    }
}

package org.example.entidades;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

public class Hotel {
    private ObjectId objectIDHotel;
    private int idHotel;
    private String nombre;
    private String telefono;
    private String email;
    private Map<String, String> direccion;
    private List<Integer> habitaciones;
    private int zona;

    public int getIdHotel() {
        return idHotel;
    }

    public ObjectId getObjectIDHotel() {
        return objectIDHotel;
    }

    public String getNombre() {
        return nombre;
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

    public List<Integer> getHabitaciones() {
        return habitaciones;
    }

    public int getZona() {
        return zona;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "idHotel=" + idHotel +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion=" + direccion+
                ", habitaciones=" + habitaciones +
                ", zona=" + zona +
                '}';
    }

    // Constructor
    public Hotel(ObjectId objectIDHotel, int idHotel, String nombre, String telefono, String email, Map<String, String> direccion, List<Integer> habitaciones, int zona) {
        this.objectIDHotel = objectIDHotel;
        this.idHotel = idHotel;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.habitaciones = habitaciones;
        this.zona = zona;
    }
    
}

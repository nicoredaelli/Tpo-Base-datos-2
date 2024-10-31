package org.example.entidades;



import java.util.List;

public class Hotel {
    private String idHotel;
    private String nombre;
    private String telefono;
    private String email;
    private Direccion direccion;
    private List<String> habitaciones;
    private String idZona;

    // Constructor
    public Hotel(String idHotel, String nombre, String telefono, String email, Direccion direccion, List<String> habitaciones, String idZona) {
        this.idHotel = idHotel;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.habitaciones = habitaciones;
        this.idZona = idZona;
    }

    // Getters y Setters
    public String getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(String idHotel) {
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

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<String> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<String> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }
}

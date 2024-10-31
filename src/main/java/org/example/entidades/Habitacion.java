package org.example.entidades;



import java.util.List;

public class Habitacion {
    private String nroHabitacion;
    private String idHotel;
    private String tipoHabitacion;
    private List<String> amenities;

    // Constructor
    public Habitacion(String nroHabitacion, String idHotel, String tipoHabitacion, List<String> amenities) {
        this.nroHabitacion = nroHabitacion;
        this.idHotel = idHotel;
        this.tipoHabitacion = tipoHabitacion;
        this.amenities = amenities;
    }

    // Getters y Setters
    public String getNroHabitacion() {
        return nroHabitacion;
    }

    public void setNroHabitacion(String nroHabitacion) {
        this.nroHabitacion = nroHabitacion;
    }

    public String getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(String idHotel) {
        this.idHotel = idHotel;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
}

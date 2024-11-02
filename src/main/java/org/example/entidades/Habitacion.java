package org.example.entidades;

import java.util.List;

import org.bson.types.ObjectId;

public class Habitacion {
    private int nroHabitacion;
    private int idHotel;
    private String tipoHabitacion;
    private List<Integer> amenities;

    public int getNroHabitacion() {
        return nroHabitacion;
    }

    public void setNroHabitacion(int nroHabitacion) {
        this.nroHabitacion = nroHabitacion;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public List<Integer> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Integer> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "nroHabitacion=" + nroHabitacion +
                ", idHotel=" + idHotel +
                ", tipoHabitacion='" + tipoHabitacion + '\'' +
                ", amenities=" + amenities +
                '}';
    }

    // Constructor
    public Habitacion(int nroHabitacion, int idHotel, String tipoHabitacion, List<Integer> amenities) {
        this.nroHabitacion = nroHabitacion;
        this.idHotel = idHotel;
        this.tipoHabitacion = tipoHabitacion;
        this.amenities = amenities;
    }
}

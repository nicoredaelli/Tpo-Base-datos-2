package org.example.entidades;



import java.util.Date;

public class Reserva {
    private String codigoReserva;
    private Date fechaInicio;
    private Date fechaSalida;
    private String hotelId;
    private String habitacionId;
    private String huespedId;

    // Constructor
    public Reserva(String codigoReserva, Date fechaInicio, Date fechaSalida, String hotelId, String habitacionId, String huespedId) {
        this.codigoReserva = codigoReserva;
        this.fechaInicio = fechaInicio;
        this.fechaSalida = fechaSalida;
        this.hotelId = hotelId;
        this.habitacionId = habitacionId;
        this.huespedId = huespedId;
    }

    // Getters y Setters
    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(String habitacionId) {
        this.habitacionId = habitacionId;
    }

    public String getHuespedId() {
        return huespedId;
    }

    public void setHuespedId(String huespedId) {
        this.huespedId = huespedId;
    }
}


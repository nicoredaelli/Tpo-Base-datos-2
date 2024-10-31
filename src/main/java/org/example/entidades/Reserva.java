package org.example.entidades;


import org.bson.types.ObjectId;

public class Reserva {
     private int codReserva;
    private String checkin;
    private String checkout;
    private String estadoReserva;
    private double tarifa;
    private ObjectId idHotel;
    private int idHabitacion;
    private ObjectId idHuesped;

    public int getCodReserva() {
        return codReserva;
    }
 
    public void setCodReserva(int codReserva) {
        this.codReserva = codReserva;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public ObjectId getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(ObjectId idHotel) {
        this.idHotel = idHotel;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public ObjectId getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(ObjectId idHuesped) {
        this.idHuesped = idHuesped;
    }

    // Constructor
    public Reserva(int codReserva, String checkin, String checkout, String estadoReserva, double tarifa, ObjectId idHotel, int idHabitacion, ObjectId idHuesped) {
        this.codReserva = codReserva;
        this.checkin = checkin;
        this.checkout = checkout;
        this.estadoReserva = estadoReserva;
        this.tarifa = tarifa;
        this.idHotel = idHotel;
        this.idHabitacion = idHabitacion;
        this.idHuesped = idHuesped;
    }
}


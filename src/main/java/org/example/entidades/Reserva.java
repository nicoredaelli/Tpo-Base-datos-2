package org.example.entidades;


import org.bson.types.ObjectId;

import java.util.Date;

public class Reserva {
     private int codReserva;
    private Date checkin;
    private Date checkout;
    private EstadoReserva estadoReserva;
    private double tarifa;
    private int idHotel;
    private int idHabitacion;
    private int idHuesped;

    public int getCodReserva() {
        return codReserva;
    }
 
    public void setCodReserva(int codReserva) {
        this.codReserva = codReserva;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public Date getCheckout() {
        return checkout;
    }

    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(int idHuesped) {
        this.idHuesped = idHuesped;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "codReserva=" + codReserva +
                ", checkin='" + checkin + '\'' +
                ", checkout='" + checkout + '\'' +
                ", estadoReserva=" + estadoReserva +
                ", tarifa=" + tarifa +
                ", idHotel=" + idHotel +
                ", idHabitacion=" + idHabitacion +
                ", idHuesped=" + idHuesped +
                '}';
    }

    // Constructor
    public Reserva(int codReserva, Date checkin, Date checkout, EstadoReserva estadoReserva, double tarifa, int idHotel, int idHabitacion, int idHuesped) {
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


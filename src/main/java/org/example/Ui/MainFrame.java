package org.example.Ui;

import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import lombok.Getter;
import lombok.Setter;
import org.example.Ui.Metodo10.ReservasPorFechaPanel;
import org.example.Ui.Metodo11.DetallesHuespedPanel;
import org.example.Ui.Metodo2.*;
import org.example.Ui.Metodo2.CrudHuesped.*;
import org.example.Ui.Metodo2.CrudReserva.*;
import org.example.Ui.Metodo3.HotelesCercanosPOIPanel;
import org.example.Ui.Metodo5.PuntosInteresCercanosHotelPanel;
import org.example.Ui.Metodo7.AmenitiesHabitacionPanel;
import org.example.Ui.Metodo1.*;
import org.example.Ui.Metodo1.CrudAmenitie.*;
import org.example.Ui.Metodo1.CrudHabitaciones.*;
import org.example.Ui.Metodo1.CrudHotel.*;
import org.example.Ui.Metodo1.CrudPOI.*;
import org.example.Ui.Metodo4.InfoHotelPanel;
import org.example.Ui.Metodo6.BuscarHabitacionDisponiblePanel;
import org.example.Ui.Metodo8.ReservasConfirmadasPanel;
import org.example.Ui.Metodo9.ReservasPorHuespedPanel;

import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Agregar paneles con nombres correctos
        mainPanel.add(new MenuPanel(this), "Menu");

        //---------------------------------------EJERCICIO 1 ---------------------------------------------------------------------------------------------------------------------------------------
        mainPanel.add(new GestiónHHAP(this), "GestiónHHAP"); // Panel para la gestión

        mainPanel.add(new HotelCRUDPanel(this), "HotelCRUDPanel");
        mainPanel.add(new CreateHotelPanel(this), "CreateHotel");
        mainPanel.add(new UpdateHotelPanel(this), "UpdateHotel");
        mainPanel.add(new DeleteHotelPanel(this), "DeleteHotel");
        mainPanel.add(new ReadHotelPanel(this), "ReadHotel");

        mainPanel.add(new RoomCRUDPanel(this), "RoomCRUDPanel");
        mainPanel.add(new CreateRoomPanel(this), "CreateRoomPanel");
        mainPanel.add(new DeleteRoomPanel(this), "DeleteRoomPanel");
        mainPanel.add(new UpdateRoomPanel(this), "UpdateRoomPanel");
        mainPanel.add(new RoomSelectionPanel(this), "RoomSelectionPanel");

        mainPanel.add(new DeleteRoomHotelSelectionPanel(this), "DeleteRoomHotelSelectionPanel");
        mainPanel.add(new UpdateRoomHotelSelectionPanel(this), "UpdateRoomHotelSelectionPanel");

        mainPanel.add(new AmenityCRUDPanel(this), "AmenityCRUDPanel");
        mainPanel.add(new CreateAmenitiePanel(this), "CreateAmenitiePanel");
        mainPanel.add(new UpdateAmenitiePanel(this), "UpdateAmenitiePanel");
        mainPanel.add(new DeleteAmenitiePanel(this), "DeleteAmenitiePanel");
        mainPanel.add(new ReadAmenitiePanel(this), "ReadAmenitiePanel");

        mainPanel.add(new POICRUDPanel(this), "POICRUDPanel");
        mainPanel.add(new CreatePOIPanel(this), "CreatePOIPanel");
        mainPanel.add(new ReadPOIPanel(this), "ReadPOIPanel");
        mainPanel.add(new UpdatePOIPanel(this), "UpdatePOIPanel");
        mainPanel.add(new DeletePOIPanel(this), "DeletePOIPanel");

        //---------------------------------------EJERCICIO 2 ---------------------------------------------------------------------------------------------------------------------------------------
        mainPanel.add(new GestiónHR(this), "GestiónHR"); // Panel para la gestión

        mainPanel.add(new ReservaCRUDPanel(this), "ReservaCRUDPanel");
        mainPanel.add(new CreateReservaPanel(this), "CreateReservaPanel");
        mainPanel.add(new ReadReservaPanel(this), "ReadReservaPanel");
        mainPanel.add(new UpdateReservaPanel(this), "UpdateReservaPanel");
        mainPanel.add(new DeleteReservaPanel(this), "DeleteReservaPanel");

        mainPanel.add(new HuespedCRUDPanel(this), "HuespedCRUDPanel");
        mainPanel.add(new CreateHuespedPanel(this), "CreateHuespedPanel");
        mainPanel.add(new ReadHuespedPanel(this), "ReadHuespedPanel");
        mainPanel.add(new UpdateHuespedPanel(this), "UpdateHuespedPanel");
        mainPanel.add(new DeleteHuespedPanel(this), "DeleteHuespedPanel");

        //---------------------------------------EJERCICIO 3 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new HotelesCercanosPOIPanel(this), "HotelesCercanosPOI");

        //---------------------------------------EJERCICIO 4 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new InfoHotelPanel(this), "InfoHotelPanel");

        //---------------------------------------EJERCICIO 5 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new PuntosInteresCercanosHotelPanel(this), "PuntosInteresCercanosHotel");

        //---------------------------------------EJERCICIO 6 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new BuscarHabitacionDisponiblePanel(this), "BuscarHabitacionDisponiblePanel");

        //---------------------------------------EJERCICIO 7 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new AmenitiesHabitacionPanel(this), "AmenitiesHabitacion");

        //---------------------------------------EJERCICIO 8 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new ReservasConfirmadasPanel(this), "ReservasConfirmadasPanel");

        //---------------------------------------EJERCICIO 9 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new ReservasPorHuespedPanel(this), "ReservasPorHuespedPanel");

        //---------------------------------------EJERCICIO 10 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new ReservasPorFechaPanel(this), "ReservasPorFechaPanel");

        //---------------------------------------EJERCICIO 11 ---------------------------------------------------------------------------------------------------------------------------------------

        mainPanel.add(new DetallesHuespedPanel(this), "DetallesHuespedPanel");

        add(mainPanel);
        setTitle("Administrador de Hoteles");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // Método sobrecargado para abrir ReadRoomPanel con un hotelId específico
    public void showRoomPanel(int hotelId) {
        ReadRoomPanel readRoomPanel = new ReadRoomPanel(this, hotelId);
        mainPanel.add(readRoomPanel, "ReadRoomPanel");
        showPanel("ReadRoomPanel");
    }

    public void showDeleteRoomPanel(int hotelId) {
        // Crear una instancia de DeleteRoomPanel y cargar las habitaciones correspondientes al hotel
        DeleteRoomPanel deleteRoomPanel = new DeleteRoomPanel(this);
        deleteRoomPanel.loadRooms(hotelId); // Cargar las habitaciones para el hotel seleccionado

        // Agregar el nuevo panel al mainPanel con el nombre "DeleteRoomPanel" si aún no existe
        mainPanel.add(deleteRoomPanel, "DeleteRoomPanel");

        // Mostrar el panel de eliminación de habitaciones
        showPanel("DeleteRoomPanel");
    }

    public void showUpdateRoomPanel(int hotelId) {
        // Crear una instancia de UpdateRoomPanel y cargar las habitaciones correspondientes al hotel
        UpdateRoomPanel updateRoomPanel = new UpdateRoomPanel(this);
        updateRoomPanel.loadRooms(hotelId); // Cargar las habitaciones para el hotel seleccionado

        // Agregar el nuevo panel al mainPanel con el nombre "UpdateRoomPanel" si aún no existe
        mainPanel.add(updateRoomPanel, "UpdateRoomPanel");

        // Mostrar el panel de actualización de habitaciones
        showPanel("UpdateRoomPanel");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}




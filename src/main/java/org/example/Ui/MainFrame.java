package org.example.Ui;

import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.Ui.CrudAmenitie.*;
import org.example.Ui.CrudHabitaciones.CreateRoomPanel;
import org.example.Ui.CrudHabitaciones.DeleteRoomPanel;
import org.example.Ui.CrudHabitaciones.ReedRoomPanel;
import org.example.Ui.CrudHabitaciones.RoomCRUDPanel;
import org.example.Ui.CrudHabitaciones.UpdateRoomPanel;
import org.example.Ui.CrudHotel.*;

import java.awt.CardLayout;
import io.mateu.dtos.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Agregar paneles con nombres correctos
        mainPanel.add(new MenuPanel(this), "Menu");
        mainPanel.add(new GestiónHHAP(this), "GestiónHHAP"); // Panel para la gestión

        mainPanel.add(new HotelCRUDPanel(this), "HotelCRUDPanel");
        mainPanel.add(new CreateHotelPanel(this), "CreateHotel"); // Panel para crear hotel
        mainPanel.add(new UpdateHotelPanel(this), "UpdateHotel");
        mainPanel.add(new DeleteHotelPanel(this), "DeleteHotel");
        mainPanel.add(new ReadHotelPanel(this), "ReadHotel");

        mainPanel.add(new RoomCRUDPanel(this), "RoomCRUDPanel");
        mainPanel.add(new CreateRoomPanel(this), "CreateRoomPanel");
        mainPanel.add(new ReedRoomPanel(this), "ReedRoomPanel");
        mainPanel.add(new DeleteRoomPanel(this), "DeleteRoomPanel");
        mainPanel.add(new UpdateRoomPanel(this), "UpdateRoomPanel");

        mainPanel.add(new AmenityCRUDPanel(this), "AmenityCRUDPanel");
        mainPanel.add(new CreateAmenitiePanel(this), "CreateAmenitiePanel");
        mainPanel.add(new UpdateAmenitiePanel(this), "UpdateAmenitiePanel");
        mainPanel.add(new DeleteAmenitiePanel(this), "DeleteAmenitiePanel");
        mainPanel.add(new ReadAmenitiePanel(this), "ReadAmenitiePanel");

        add(mainPanel);
        setTitle("Administrador de Hoteles");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName); // Cambia al panel correspondiente
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

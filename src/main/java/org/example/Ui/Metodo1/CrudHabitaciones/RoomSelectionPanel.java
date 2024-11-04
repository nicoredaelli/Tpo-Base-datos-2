package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;


import javax.swing.*;
import java.awt.*;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RoomSelectionPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;
    private MainFrame mainFrame;

    public RoomSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        hotelDropdown = new JComboBox<>();

        // Llenar el JComboBox con los hoteles disponibles
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }

        JButton selectHotelButton = new JButton("Seleccionar Hotel");
        selectHotelButton.addActionListener(this::selectHotel);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(new JLabel("Seleccione un hotel:"));
        add(hotelDropdown);
        add(selectHotelButton);
        add(backButton);
    }

    private void selectHotel(ActionEvent e) {
        String selectedHotel = (String) hotelDropdown.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            // Crear y mostrar el ReadRoomPanel con el hotelId seleccionado
            mainFrame.showRoomPanel(hotelId);
            mainFrame.showPanel("ReadRoomPanel");
        }
    }
}








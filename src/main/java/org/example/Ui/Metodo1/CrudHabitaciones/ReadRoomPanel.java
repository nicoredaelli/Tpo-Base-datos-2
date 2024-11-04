package org.example.Ui.Metodo1.CrudHabitaciones;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
import org.example.entidades.Habitacion;

import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.*;

public class ReadRoomPanel extends JPanel {
    private JComboBox<String> roomDropdown;
    private JTextArea roomDetailsArea;
    private CRUDController crudController;
    private MainFrame mainFrame;
    private int hotelId;

    public ReadRoomPanel(MainFrame mainFrame, int hotelId) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();
        this.hotelId = hotelId;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomDropdown = new JComboBox<>();

        // Llenar el JComboBox con las habitaciones del hotel seleccionado
        loadRoomDropdown();

        roomDropdown.addActionListener(this::loadRoomDetails);

        roomDetailsArea = new JTextArea(10, 30);
        roomDetailsArea.setEditable(false);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomSelectionPanel"));

        add(new JLabel("Seleccione una habitación:"));
        add(roomDropdown);
        add(new JScrollPane(roomDetailsArea));
        add(backButton);
    }

    private void loadRoomDropdown() {
        roomDropdown.removeAllItems();  // Limpiar el JComboBox
        List<Habitacion> habitaciones = crudController.getRoomsByHotelId(hotelId);
        for (Habitacion habitacion : habitaciones) {
            roomDropdown.addItem(habitacion.getIdHabitacion() + " - " + habitacion.getNroHabitacion() + " (" + habitacion.getTipoHabitacion() + ")");
        }
    }

    private void loadRoomDetails(ActionEvent e) {
        String selectedRoom = (String) roomDropdown.getSelectedItem();
        if (selectedRoom != null) {
            int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);
            Habitacion habitacion = crudController.readHabitacion(roomId);
            if (habitacion != null) {
                StringBuilder details = new StringBuilder();
                details.append("ID Habitación: ").append(habitacion.getIdHabitacion()).append("\n");
                details.append("Número de Habitación: ").append(habitacion.getNroHabitacion()).append("\n");
                details.append("Tipo de Habitación: ").append(habitacion.getTipoHabitacion()).append("\n");
                details.append("ID Hotel: ").append(habitacion.getIdHotel()).append("\n");
                details.append("Amenities:\n");

                // Obtener y mostrar detalles de cada amenity
                for (Integer amenityId : habitacion.getAmenities()) {
                    Amenity amenity = crudController.readAmenity(amenityId);
                    if (amenity != null) {
                        details.append("  - ").append(amenity.getNombre()).append(": ").append(amenity.getDescripcion()).append("\n");
                    }
                }

                roomDetailsArea.setText(details.toString());
            } else {
                roomDetailsArea.setText("No se encontró información de la habitación.");
            }
        }
    }
}








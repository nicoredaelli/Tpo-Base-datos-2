package org.example.Ui.Metodo1.CrudHabitaciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;

public class UpdateRoomPanel extends JPanel {
    private JTextField roomNumberField, roomTypeField, amenitiesField;
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;

    public UpdateRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomNumberField = new JTextField(10);
        hotelDropdown = new JComboBox<>();
        roomTypeField = new JTextField(20);
        amenitiesField = new JTextField(20);

        // Cargar hoteles en el JComboBox
        List<Hotel> hotelesDisponibles = crudController.getHotelesDisponibles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }

        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("Hotel:"));
        add(hotelDropdown);
        add(new JLabel("Tipo de Habitación:"));
        add(roomTypeField);
        add(new JLabel("Amenities (IDs separados por coma):"));
        add(amenitiesField);

        JButton loadButton = new JButton("Cargar Datos");
        loadButton.addActionListener(e -> loadRoomData());

        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> updateRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(loadButton);
        add(updateButton);
        add(backButton);
    }

    private void loadRoomData() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            Habitacion habitacion = crudController.readHabitacion(roomNumber);

            if (habitacion != null) {
                roomTypeField.setText(habitacion.getTipoHabitacion());
                amenitiesField.setText(habitacion.getAmenities().toString().replaceAll("[\\[\\] ]", ""));
            } else {
                JOptionPane.showMessageDialog(this, "Habitación no encontrada.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de habitación válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);
            String roomType = roomTypeField.getText();

            List<Integer> amenities = new ArrayList<>();
            if (!amenitiesField.getText().trim().isEmpty()) {
                amenities = Arrays.stream(amenitiesField.getText().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
            }

            Habitacion habitacionActualizada = new Habitacion(roomNumber, hotelId, roomType, amenities);
            crudController.updateHabitacion(habitacionActualizada);

            JOptionPane.showMessageDialog(this, "Habitación actualizada exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

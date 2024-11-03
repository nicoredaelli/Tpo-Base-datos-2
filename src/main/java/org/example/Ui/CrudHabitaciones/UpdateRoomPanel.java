package org.example.Ui.CrudHabitaciones;


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

public class UpdateRoomPanel extends JPanel {
   private JTextField roomNumberField, hotelIdField, roomTypeField, amenitiesField;
    private CRUDController crudController;

    public UpdateRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomNumberField = new JTextField(10);
        hotelIdField = new JTextField(10);
        roomTypeField = new JTextField(20);
        amenitiesField = new JTextField(20);

        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("ID del Hotel:"));
        add(hotelIdField);
        add(new JLabel("Tipo de Habitación:"));
        add(roomTypeField);
        add(new JLabel("Amenities (IDs separados por coma):"));
        add(amenitiesField);

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(e -> createRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel")); // Ajustar nombre según el panel anterior

        add(createButton);
        add(backButton);
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            int hotelId = Integer.parseInt(hotelIdField.getText());
            String roomType = roomTypeField.getText();
            
            // Convertir los IDs de amenities a una lista de enteros
            List<Integer> amenities = new ArrayList<>();
            if (!amenitiesField.getText().trim().isEmpty()) {
                amenities = Arrays.stream(amenitiesField.getText().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
            }

            // Crear la habitación
            Habitacion nuevaHabitacion = new Habitacion(roomNumber, hotelId, roomType, amenities);
            crudController.updateHabitacion(nuevaHabitacion);

            JOptionPane.showMessageDialog(this, "Habitación actualiza exitosamente");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


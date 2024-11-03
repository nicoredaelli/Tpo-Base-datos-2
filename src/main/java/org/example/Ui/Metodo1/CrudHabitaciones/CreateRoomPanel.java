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
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateRoomPanel extends JPanel {
    private JTextField roomNumberField, roomTypeField, amenitiesField;
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;
    private List<Hotel> hotelesDisponibles; // Lista de hoteles disponibles

    public CreateRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Inicializar campos de entrada
        roomNumberField = new JTextField(10);
        roomTypeField = new JTextField(20);
        amenitiesField = new JTextField(20);

        // Obtener la lista de hoteles disponibles desde el CRUDController
        hotelesDisponibles = crudController.getHotelesDisponibles();

        // Crear la lista desplegable con los nombres de los hoteles
        hotelDropdown = new JComboBox<>(hotelesDisponibles.stream()
                .map(Hotel::getNombre)
                .toArray(String[]::new));
        
        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("Hotel:"));
        add(hotelDropdown);
        add(new JLabel("Tipo de Habitación:"));
        add(roomTypeField);
        add(new JLabel("Amenities (IDs separados por coma):"));
        add(amenitiesField);

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(e -> createRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(createButton);
        add(backButton);
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String roomType = roomTypeField.getText();
            
            // Obtener el ID del hotel seleccionado
            int hotelId = hotelesDisponibles.get(hotelDropdown.getSelectedIndex()).getIdHotel();

            // Convertir los IDs de amenities a una lista de enteros
            List<Integer> amenities = new ArrayList<>();
            if (!amenitiesField.getText().trim().isEmpty()) {
                amenities = Arrays.stream(amenitiesField.getText().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }

            // Crear la habitación
            int ultimoIDHabitacionDisponible = crudController.getUltimoIDHabitacion() + 1;
            Habitacion nuevaHabitacion = new Habitacion(ultimoIDHabitacionDisponible, roomNumber, hotelId, roomType, amenities);
            crudController.createHabitacion(nuevaHabitacion);

            JOptionPane.showMessageDialog(this, "Habitación creada exitosamente");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

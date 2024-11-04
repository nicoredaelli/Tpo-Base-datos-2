package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Amenity;
import org.example.entidades.Habitacion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateRoomPanel extends JPanel {
    private JTextField roomNumberField, roomTypeField;
    private JComboBox<String> roomDropdown;
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;
    private MainFrame mainFrame;
    private int hotelId;
    private List<JCheckBox> amenityCheckBoxes; // Lista de checkboxes para amenities

    public UpdateRoomPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();
        this.databaseQueryController = new DatabaseQueryController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomDropdown = new JComboBox<>();
        roomNumberField = new JTextField(10);
        roomTypeField = new JTextField(20);

        JButton loadButton = new JButton("Cargar Datos");
        loadButton.addActionListener(e -> loadRoomData());

        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> {
            updateRoom();
            clearFields(); // Limpiar campos después de actualizar la habitación
        });

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> {
            clearFields(); // Limpiar campos antes de regresar al panel de selección de hotel
            mainFrame.showPanel("UpdateRoomHotelSelectionPanel");
        });

        add(new JLabel("Seleccione una habitación:"));
        add(roomDropdown);
        add(loadButton);
        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("Tipo de Habitación:"));
        add(roomTypeField);

        // Cargar amenities disponibles desde el CRUDController
        List<Amenity> amenitiesDisponibles = crudController.getAllAmenities();

        // Crear panel para los checkboxes de amenities
        JPanel amenitiesPanel = new JPanel();
        amenitiesPanel.setLayout(new BoxLayout(amenitiesPanel, BoxLayout.Y_AXIS));
        amenitiesPanel.setBorder(BorderFactory.createTitledBorder("Amenities Disponibles"));

        // Crear un checkbox para cada amenity y añadirlo al panel de amenities
        amenityCheckBoxes = new ArrayList<>();
        for (Amenity amenity : amenitiesDisponibles) {
            JCheckBox checkBox = new JCheckBox(amenity.getNombre());
            checkBox.setToolTipText(amenity.getDescripcion()); // Añade descripción como tooltip
            amenityCheckBoxes.add(checkBox);
            amenitiesPanel.add(checkBox);
        }

        add(amenitiesPanel); // Añadir el panel de amenities a la interfaz

        add(updateButton);
        add(backButton);
    }

    public void loadRooms(int hotelId) {
        this.hotelId = hotelId;
        roomDropdown.removeAllItems(); // Limpiar el JComboBox

        List<Habitacion> habitaciones = crudController.getRoomsByHotelId(hotelId);
        for (Habitacion habitacion : habitaciones) {
            roomDropdown.addItem(habitacion.getIdHabitacion() + " - " + habitacion.getNroHabitacion());
        }
    }

    private void loadRoomData() {
        String selectedRoom = (String) roomDropdown.getSelectedItem();
        if (selectedRoom != null) {
            int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);
            Habitacion habitacion = crudController.readHabitacion(roomId);

            if (habitacion != null) {
                roomNumberField.setText(String.valueOf(habitacion.getNroHabitacion()));
                roomTypeField.setText(habitacion.getTipoHabitacion());

                // Actualizar los checkboxes de amenities según la habitación cargada
                for (JCheckBox checkBox : amenityCheckBoxes) {
                    int amenityId = databaseQueryController.getAmenityIdByName(checkBox.getText());
                    checkBox.setSelected(habitacion.getAmenities().contains(amenityId));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Habitación no encontrada.");
            }
        }
    }

    private void updateRoom() {
        try {
            String selectedRoom = (String) roomDropdown.getSelectedItem();
            if (selectedRoom != null) {
                int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                String roomType = roomTypeField.getText();

                // Obtener la lista de IDs de amenities seleccionados
                List<Integer> amenities = new ArrayList<>();
                for (JCheckBox checkBox : amenityCheckBoxes) {
                    if (checkBox.isSelected()) {
                        amenities.add(databaseQueryController.getAmenityIdByName(checkBox.getText()));
                    }
                }

                Habitacion habitacionActualizada = new Habitacion(roomId, roomNumber, hotelId, roomType, amenities);
                crudController.updateHabitacion(habitacionActualizada);

                JOptionPane.showMessageDialog(this, "Habitación actualizada exitosamente.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos de texto, restablecer el JComboBox y desmarcar los checkboxes
    private void clearFields() {
        roomNumberField.setText("");
        roomTypeField.setText("");
        roomDropdown.setSelectedIndex(-1); // Deseleccionar el JComboBox
        for (JCheckBox checkBox : amenityCheckBoxes) {
            checkBox.setSelected(false); // Desmarcar todos los checkboxes
        }
    }
}


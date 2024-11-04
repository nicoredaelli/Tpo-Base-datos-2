package org.example.Ui.Metodo1.CrudHabitaciones;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.stream.Collectors;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Amenity;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;

public class CreateRoomPanel extends JPanel {
    private JTextField roomNumberField, roomTypeField;
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;
    private List<Hotel> hotelesDisponibles; // Lista de hoteles disponibles
    private List<JCheckBox> amenityCheckBoxes; // Lista de checkboxes para amenities

    public CreateRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        databaseQueryController = new DatabaseQueryController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Inicializar campos de entrada
        roomNumberField = new JTextField(10);
        roomTypeField = new JTextField(20);

        // Obtener la lista de hoteles disponibles desde el CRUDController
        hotelesDisponibles = crudController.getAllHoteles();

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

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(e -> {
            createRoom();
            clearFields(); // Limpiar campos después de crear la habitación
        });

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> {
            clearFields(); // Limpiar campos antes de regresar al panel principal
            mainFrame.showPanel("RoomCRUDPanel");
        });

        add(createButton);
        add(backButton);
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String roomType = roomTypeField.getText();

            // Obtener el ID del hotel seleccionado
            int hotelId = hotelesDisponibles.get(hotelDropdown.getSelectedIndex()).getIdHotel();

            // Obtener la lista de IDs de amenities seleccionados
            List<Integer> amenities = amenityCheckBoxes.stream()
                    .filter(JCheckBox::isSelected)
                    .map(checkBox -> databaseQueryController.getAmenityIdByName(checkBox.getText())) // Suponiendo que hay un método para obtener el ID del amenity por su nombre
                    .collect(Collectors.toList());

            // Crear la habitación
            int ultimoIDHabitacionDisponible = crudController.getUltimoIDHabitacion() + 1;
            Habitacion nuevaHabitacion = new Habitacion(ultimoIDHabitacionDisponible, roomNumber, hotelId, roomType, amenities);
            crudController.createHabitacion(nuevaHabitacion);

            JOptionPane.showMessageDialog(this, "Habitación creada exitosamente");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos de texto, restablecer el desplegable y desmarcar los checkboxes
    private void clearFields() {
        roomNumberField.setText("");
        roomTypeField.setText("");
        hotelDropdown.setSelectedIndex(0);

        // Desmarcar todos los checkboxes de amenities
        for (JCheckBox checkBox : amenityCheckBoxes) {
            checkBox.setSelected(false);
        }
    }
}


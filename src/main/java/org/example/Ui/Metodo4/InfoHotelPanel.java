package org.example.Ui.Metodo4;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;
import org.example.entidades.Zona;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InfoHotelPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private JCheckBox nameCheckBox, phoneCheckBox, emailCheckBox, addressCheckBox, roomsCheckBox, zoneCheckBox;
    private JButton loadButton, backButton;
    private JTextArea resultArea;
    private CRUDController crudController;

    public InfoHotelPanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        setLayout(new BorderLayout());

        // Panel para selección de hotel y atributos
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        // Lista desplegable de hoteles
        hotelDropdown = new JComboBox<>();
        loadHotelsIntoDropdown();  // Método para cargar los hoteles en el JComboBox
        topPanel.add(new JLabel("Seleccione un Hotel:"));
        topPanel.add(hotelDropdown);

        // Panel de selección de atributos
        JPanel attributesPanel = new JPanel(new GridLayout(6, 1));
        nameCheckBox = new JCheckBox("Nombre");
        phoneCheckBox = new JCheckBox("Teléfono");
        emailCheckBox = new JCheckBox("Email");
        addressCheckBox = new JCheckBox("Dirección Completa");
        roomsCheckBox = new JCheckBox("Habitaciones");
        zoneCheckBox = new JCheckBox("Zona");

        attributesPanel.add(nameCheckBox);
        attributesPanel.add(phoneCheckBox);
        attributesPanel.add(emailCheckBox);
        attributesPanel.add(addressCheckBox);
        attributesPanel.add(roomsCheckBox);
        attributesPanel.add(zoneCheckBox);

        topPanel.add(new JLabel("Seleccione los Atributos:"));
        topPanel.add(attributesPanel);

        add(topPanel, BorderLayout.NORTH);

        // Panel para el botón de carga y área de texto de resultados
        JPanel centerPanel = new JPanel(new BorderLayout());
        loadButton = new JButton("Mostrar datos");
        loadButton.addActionListener(e -> loadHotelData());
        centerPanel.add(loadButton, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        centerPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Panel para el botón de regresar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        bottomPanel.add(backButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método para cargar los hoteles disponibles en el JComboBox
    private void loadHotelsIntoDropdown() {
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        if (hotelesDisponibles != null && !hotelesDisponibles.isEmpty()) {
            for (Hotel hotel : hotelesDisponibles) {
                hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
            }
        } else {
            hotelDropdown.addItem("No hay hoteles disponibles");
        }
    }

    private void loadHotelData() {
        try {
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            if (selectedHotel == null || selectedHotel.equals("No hay hoteles disponibles")) {
                resultArea.setText("No hay hoteles seleccionados.");
                return;
            }
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            // Llama al controlador para obtener todos los datos del hotel
            Hotel hotel = crudController.readHotel(hotelId);

            if (hotel != null) {
                // Filtra y muestra los datos según los atributos seleccionados
                displayHotelInfo(hotel);
            } else {
                resultArea.setText("Hotel no encontrado.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del hotel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayHotelInfo(Hotel hotel) {
        StringBuilder result = new StringBuilder("Información del Hotel:\n");

        // Mostrar el nombre, teléfono y otros datos básicos solo si están seleccionados
        if (nameCheckBox.isSelected() && hotel.getNombre() != null) {
            result.append("Nombre: ").append(hotel.getNombre()).append("\n");
        }
        if (phoneCheckBox.isSelected() && hotel.getTelefono() != null) {
            result.append("Teléfono: ").append(hotel.getTelefono()).append("\n");
        }
        if (emailCheckBox.isSelected() && hotel.getEmail() != null) {
            result.append("Email: ").append(hotel.getEmail()).append("\n");
        }
        if (addressCheckBox.isSelected() && hotel.getDireccion() != null) {
            result.append("Dirección:\n");
            hotel.getDireccion().forEach((key, value) -> result.append("  ").append(key).append(": ").append(value).append("\n"));
        }

        // Mostrar habitaciones y sus detalles si el checkbox está seleccionado
        if (roomsCheckBox.isSelected() && hotel.getHabitaciones() != null && !hotel.getHabitaciones().isEmpty()) {
            result.append("Habitaciones:\n");
            for (Integer idHabitacion : hotel.getHabitaciones()) {
                Habitacion habitacion = crudController.readHabitacion(idHabitacion);
                if (habitacion != null) {
                    result.append("  Habitación ")
                            .append(habitacion.getNroHabitacion()).append(" - ")
                            .append("Tipo: ").append(habitacion.getTipoHabitacion()).append(", ")
                            .append("ID Hotel: ").append(habitacion.getIdHotel()).append("\n")
                            .append("  Amenities:\n");

                    // Mostrar cada amenity en la habitación
                    for (Integer amenityId : habitacion.getAmenities()) {
                        Amenity amenity = crudController.readAmenity(amenityId);
                        if (amenity != null) {
                            result.append("    - ")
                                    .append("ID: ").append(amenity.getIdAmenity()).append(", ")
                                    .append("Nombre: ").append(amenity.getNombre()).append(", ")
                                    .append("Descripción: ").append(amenity.getDescripcion())
                                    .append("\n");
                        } else {
                            result.append("    - Amenity con ID ").append(amenityId).append(" no encontrado\n");
                        }
                    }
                } else {
                    result.append("  Habitación con ID ").append(idHabitacion).append(" no encontrada\n");
                }
            }
        }

        // Mostrar información completa de la zona si está seleccionada
        if (zoneCheckBox.isSelected() && hotel.getZona() > 0) {
            Zona zona = crudController.readZona(hotel.getZona());
            if (zona != null) {
                result.append("Zona:\n")
                        .append("  ID: ").append(zona.getIdZona()).append("\n")
                        .append("  Nombre: ").append(zona.getNombre()).append("\n")
                        .append("  Provincia: ").append(zona.getProvincia()).append("\n")
                        .append("  País: ").append(zona.getPais()).append("\n")
                        .append("  Descripción: ").append(zona.getDescripcion()).append("\n");
            } else {
                result.append("Zona no encontrada para el ID: ").append(hotel.getZona()).append("\n");
            }
        }

        // Si ningún checkbox está seleccionado, muestra un mensaje
        if (result.toString().equals("Información del Hotel:\n")) {
            result.append("No se seleccionó ningún atributo para mostrar.");
        }

        resultArea.setText(result.toString());
    }
}





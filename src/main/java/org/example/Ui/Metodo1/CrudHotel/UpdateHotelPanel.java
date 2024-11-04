package org.example.Ui.Metodo1.CrudHotel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;
import org.example.entidades.Zona;

import javax.swing.*;



public class UpdateHotelPanel extends JPanel {
    private JComboBox<String> hotelDropdown; // Cuadro desplegable para seleccionar el hotel
    private JTextField nameField, phoneField, emailField, streetField, numberField, postalCodeField, provinceField, countryField;
    private JComboBox<String> zoneDropdown; // Cuadro desplegable para seleccionar la zona

    CRUDController crudController = new CRUDController();

    public UpdateHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de hoteles disponibles desde el CRUDController
        List<Hotel> hotelesDisponibles = crudController.getHotelesDisponibles();
        String[] hotelNames = hotelesDisponibles.stream().map(Hotel::getNombre).toArray(String[]::new);

        // Crear el JComboBox para seleccionar el hotel
        hotelDropdown = new JComboBox<>(hotelNames);
        add(new JLabel("Seleccione un hotel:"));
        add(hotelDropdown);

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        streetField = new JTextField(20);
        numberField = new JTextField(5);
        postalCodeField = new JTextField(5);
        provinceField = new JTextField(20);
        countryField = new JTextField(20);

        add(new JLabel("Nombre:"));
        add(nameField);
        add(new JLabel("Teléfono:"));
        add(phoneField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Calle:"));
        add(streetField);
        add(new JLabel("Número:"));
        add(numberField);
        add(new JLabel("Código Postal:"));
        add(postalCodeField);
        add(new JLabel("Provincia:"));
        add(provinceField);
        add(new JLabel("País:"));
        add(countryField);

        // Obtener la lista de zonas disponibles desde el CRUDController
        List<Zona> zonasDisponibles = crudController.getZonasDisponibles();
        String[] zonaNames = zonasDisponibles.stream().map(Zona::getNombre).toArray(String[]::new);
        zoneDropdown = new JComboBox<>(zonaNames);
        add(new JLabel("Zona:"));
        add(zoneDropdown);

        JButton loadButton = new JButton("Cargar Datos");
        loadButton.addActionListener(e -> loadHotelData());

        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> {
            actualizarHotel();
            clearFields();
        });

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> {
            mainFrame.showPanel("HotelCRUDPanel");
            clearFields();
        });


        add(loadButton);
        add(updateButton);
        add(backButton);
    }

    private void loadHotelData() {
        String selectedHotelName = (String) hotelDropdown.getSelectedItem();
        Hotel hotel = crudController.getHotelesDisponibles().stream()
            .filter(h -> h.getNombre().equals(selectedHotelName))
            .findFirst()
            .orElse(null);

        if (hotel != null) {
            nameField.setText(hotel.getNombre());
            phoneField.setText(hotel.getTelefono());
            emailField.setText(hotel.getEmail());

            Map<String, String> direccion = hotel.getDireccion(); // Supón que este método existe
            streetField.setText(direccion.get("calle"));
            numberField.setText(direccion.get("numero"));
            postalCodeField.setText(direccion.get("codigo_postal"));
            provinceField.setText(direccion.get("provincia"));
            countryField.setText(direccion.get("pais"));

            // Obtener el ID de la zona seleccionada
            int zonaId = hotel.getZona();
            String zonaNombre = crudController.getZonasDisponibles().stream()
                .filter(z -> z.getIdZona() == zonaId)
                .map(Zona::getNombre)
                .findFirst()
                .orElse("");
            zoneDropdown.setSelectedItem(zonaNombre);
        } else {
            JOptionPane.showMessageDialog(this, "Hotel no encontrado.");
        }
    }

    private void actualizarHotel() {
        try {
            String selectedHotelName = (String) hotelDropdown.getSelectedItem();
            Hotel hotel = crudController.getHotelesDisponibles().stream()
                .filter(h -> h.getNombre().equals(selectedHotelName))
                .findFirst()
                .orElse(null);

            if (hotel == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un hotel válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idHotel = hotel.getIdHotel(); // Obtener el ID del hotel seleccionado
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String street = streetField.getText();
            String number = numberField.getText();
            String postalCode = postalCodeField.getText();
            String province = provinceField.getText();
            String country = countryField.getText();
            String selectedZone = (String) zoneDropdown.getSelectedItem();
            int zoneId = crudController.getZonasDisponibles().stream()
                .filter(z -> z.getNombre().equals(selectedZone))
                .map(Zona::getIdZona)
                .findFirst()
                .orElse(-1); // Manejar caso donde no se encuentra la zona

            // Crear una dirección ficticia para el hotel
            Map<String, String> direccion = new HashMap<>();
            direccion.put("calle", street);
            direccion.put("numero", number);
            direccion.put("codigo_postal", postalCode);
            direccion.put("provincia", province);
            direccion.put("pais", country);

            // Actualizar el hotel


            Hotel hotel = crudController.readHotel(id);

            crudController.updateHotel(id, name, phone, direccion, email, hotel.getHabitaciones(), zone);
            

            crudController.updateHotel(idHotel, name, phone, direccion, email, zoneId); // Pasar el ID del hotel

            JOptionPane.showMessageDialog(this, "Hotel actualizado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos de texto y restablecer el desplegable
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        streetField.setText("");
        numberField.setText("");
        postalCodeField.setText("");
        provinceField.setText("");
        countryField.setText("");
        
    }
}


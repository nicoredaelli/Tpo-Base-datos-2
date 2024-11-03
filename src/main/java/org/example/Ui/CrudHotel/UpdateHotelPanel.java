package org.example.Ui.CrudHotel;

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
import org.example.entidades.Hotel;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;

public class UpdateHotelPanel extends JPanel {
    private JTextField idField, nameField, phoneField, emailField, streetField, numberField, postalCodeField, provinceField, countryField, zoneField;
    
    CRUDController crudController = new CRUDController();
    
    public UpdateHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        streetField = new JTextField(20);
        numberField = new JTextField(5);
        postalCodeField = new JTextField(5);
        provinceField = new JTextField(20);
        countryField = new JTextField(20);
        zoneField = new JTextField(20);
        
        add(new JLabel("Id del hotel a actualizar:"));
        add(idField);
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
        add(new JLabel("Zona:"));
        add(zoneField);

        JButton loadButton = new JButton("Cargar Datos");
        loadButton.addActionListener(e -> loadHotelData());

        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> actualizarHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel")); // Cambiar a "GestiónHHAP" si es necesario

        add(loadButton);
        add(updateButton);
        add(backButton);
    }
    
    private void loadHotelData() {
        try {
            int id = Integer.parseInt(idField.getText());
            // Cargar los datos del hotel
            Hotel hotel = crudController.readHotel(id); // Método para buscar el hotel

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
                
                zoneField.setText(String.valueOf(hotel.getZona())); // Suponiendo que zona es un entero
            } else {
                JOptionPane.showMessageDialog(this, "Hotel no encontrado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de hotel válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarHotel() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String street = streetField.getText();
            String number = numberField.getText();
            String postalCode = postalCodeField.getText();
            String province = provinceField.getText();
            String country = countryField.getText();
            int zone = Integer.parseInt(zoneField.getText());

            // Crear una dirección ficticia para el hotel
            Map<String, String> direccion = new HashMap<>();
            direccion.put("calle", street);
            direccion.put("numero", number);
            direccion.put("codigo_postal", postalCode);
            direccion.put("provincia", province);
            direccion.put("pais", country);

            // Actualizar el hotel
            crudController.updateHotel(id, name, phone, direccion, email, zone);
            
            JOptionPane.showMessageDialog(this, "Hotel actualizado exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos en los campos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

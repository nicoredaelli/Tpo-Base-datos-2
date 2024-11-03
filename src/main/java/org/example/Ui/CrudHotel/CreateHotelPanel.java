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

public class CreateHotelPanel extends JPanel {
    private JTextField nameField, phoneField, emailField, streetField, numberField, postalCodeField, provinceField, countryField, zoneField;
    
    CRUDController crudController = new CRUDController();
    public CreateHotelPanel(MainFrame mainFrame) {
        

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        streetField = new JTextField(20);
        numberField = new JTextField(5);
        postalCodeField = new JTextField(5);
        provinceField = new JTextField(20);
        countryField = new JTextField(20);
        zoneField = new JTextField(20);
        

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
        

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(e -> createHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel")); // Cambiar a "GestiónHHAP" si es necesario

        add(createButton);
        add(backButton);
    }
    
    private void createHotel() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String street = streetField.getText();
        String number = numberField.getText();
        String postalCode = postalCodeField.getText();
        String province = provinceField.getText();
        String country = countryField.getText();
        int zone = Integer.parseInt(zoneField.getText());
       
        // Llamar al método del controlador
        int idHotel = crudController.getUltimoIdHotel(); // Trae el ultimo ID existente de los hoteles (coleccion contadores)
        idHotel++; // el siguiente del ultimo en uso

        // Crear una dirección ficticia para el hotel
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", street);
        direccion.put("numero", number);
        direccion.put("codigo_postal", postalCode);
        direccion.put("provincia", province);
        direccion.put("pais", country);

         // Crear una lista de IDs de habitaciones vacía
        List<Integer> roomIds = new ArrayList<>();

         
        Hotel nuevoHotel = new Hotel(
            new ObjectId(),        // Generar un nuevo ObjectId
                idHotel,                     // idHotel, por ejemplo
                name,       // Nombre del hotel
                phone,       // Teléfono
                email,  // Email
            direccion,             // Dirección
            roomIds,          // Habitaciones
            zone                     // ID de la zona
        );
        crudController.createHotel(nuevoHotel);
        
        JOptionPane.showMessageDialog(this, "Hotel creado exitosamente");
    }
}

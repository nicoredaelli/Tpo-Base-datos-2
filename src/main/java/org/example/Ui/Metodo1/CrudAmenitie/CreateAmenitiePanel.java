package org.example.Ui.Metodo1.CrudAmenitie;

import javax.swing.*;
import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;

public class CreateAmenitiePanel extends JPanel{
    private JTextField nameField, descriptionField;
    private CRUDController amenityCRUD;

    public CreateAmenitiePanel(MainFrame mainFrame) {
        amenityCRUD = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nameField = new JTextField(20);
        descriptionField = new JTextField(30);

        add(new JLabel("Nombre del Amenity:"));
        add(nameField);
        add(new JLabel("Descripción del Amenity:"));
        add(descriptionField);

        JButton createButton = new JButton("Crear");
        createButton.addActionListener(e -> createAmenity());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel")); // Ajustar según el panel anterior

        add(createButton);
        add(backButton);
    }

    private void createAmenity() {
        try {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener el próximo ID disponible para el amenity
            int idAmenity = amenityCRUD.getUltimoIdAmenity() + 1;

            // Crear el nuevo amenity
            Amenity nuevoAmenity = new Amenity(new ObjectId(), idAmenity, name, description);
            amenityCRUD.createAmenity(nuevoAmenity);

            JOptionPane.showMessageDialog(this, "Amenity creado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear el amenity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 

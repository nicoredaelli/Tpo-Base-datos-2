package org.example.Ui.CrudAmenitie;

import javax.swing.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
// hay que buscar un getidamenitie para que te traiga toda la info de ese amenitie 
public class UpdateAmenityPanel extends JPanel {
    private JTextField idField, nameField, descriptionField;
    private CRUDController amenityCRUD;

    public UpdateAmenityPanel(MainFrame mainFrame) {
        amenityCRUD = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(5);
        nameField = new JTextField(20);
        descriptionField = new JTextField(30);

        add(new JLabel("ID del Amenity a Actualizar:"));
        add(idField);

        add(new JLabel("Nombre del Amenity:"));
        add(nameField);

        add(new JLabel("Descripción del Amenity:"));
        add(descriptionField);

        JButton findButton = new JButton("Buscar");
        findButton.addActionListener(e -> loadAmenity());

        JButton updateButton = new JButton("Actualizar");
        updateButton.addActionListener(e -> updateAmenity());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel")); // Ajusta según el panel anterior

        add(findButton);
        add(updateButton);
        add(backButton);
    }

    private void loadAmenity() {
        try {
            int idAmenity = Integer.parseInt(idField.getText().trim());
            Amenity amenity = amenityCRUD.findAmenityById(idAmenity);

            if (amenity != null) {
                nameField.setText(amenity.getNombre());
                descriptionField.setText(amenity.getDescripcion());
            } else {
                JOptionPane.showMessageDialog(this, "Amenity no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAmenity() {
        try {
            int idAmenity = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar y actualizar el amenity
            Amenity amenity = amenityCRUD.findAmenityById(idAmenity);
            if (amenity != null) {
                amenity.setNombre(name);
                amenity.setDescripcion(description);
                amenityCRUD.updateAmenity(amenity);

                JOptionPane.showMessageDialog(this, "Amenity actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Amenity no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

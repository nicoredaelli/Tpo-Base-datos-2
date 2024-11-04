package org.example.Ui.Metodo1.CrudAmenitie;


import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;

import java.util.List;

public class DeleteAmenitiePanel extends JPanel {
    private JComboBox<String> amenityComboBox; // ComboBox para seleccionar el amenity
    private CRUDController crudController;

    public DeleteAmenitiePanel(MainFrame mainFrame) {
        // Inicializar el controlador CRUD
        this.crudController = new CRUDController();
        
        // Configuración del diseño
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Crear y agregar componentes de la interfaz
        JLabel selectLabel = new JLabel("Selecciona el Amenity a Eliminar:");
        
        // Obtener la lista de amenities y llenar el combo box
        List<Amenity> amenities = crudController.getAllAmenities();
        String[] amenityNames = amenities.stream().map(Amenity::getNombre).toArray(String[]::new);
        
        amenityComboBox = new JComboBox<>(amenityNames); // Crear el combo box con los nombres de amenities
        JButton deleteButton = new JButton("Eliminar");
        JButton backButton = new JButton("Regresar");

        // Acción del botón Eliminar
        deleteButton.addActionListener(e -> eliminarAmenity(amenities));

        // Acción del botón Regresar
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel"));

        // Añadir componentes al panel
        add(selectLabel);
        add(amenityComboBox);
        add(deleteButton);
        add(backButton);

        // Establecer alineación central de los componentes
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void eliminarAmenity(List<Amenity> amenities) {
        // Obtener el nombre seleccionado del combo box
        String selectedAmenityName = (String) amenityComboBox.getSelectedItem();
        
        // Buscar el ID correspondiente al nombre seleccionado
        Amenity amenityToDelete = amenities.stream()
                .filter(amenity -> amenity.getNombre().equals(selectedAmenityName))
                .findFirst()
                .orElse(null);
        
        if (amenityToDelete != null) {
            int idAmenity = amenityToDelete.getIdAmenity();
            // Llamar al método de eliminación en el controlador
            crudController.deleteAmenity(idAmenity);
            JOptionPane.showMessageDialog(this, "Amenity " + selectedAmenityName + " eliminado exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar el amenity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}




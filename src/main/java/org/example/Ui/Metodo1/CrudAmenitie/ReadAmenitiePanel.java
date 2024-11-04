package org.example.Ui.Metodo1.CrudAmenitie;



import javax.swing.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
import java.awt.Dimension;

import java.util.List;

public class ReadAmenitiePanel extends JPanel {
    private JComboBox<String> amenityDropdown;
    private JTextArea resultArea;
    private CRUDController crudController;

    public ReadAmenitiePanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de amenities disponibles desde el CRUDController
        List<Amenity> amenitiesDisponibles = crudController.getAllAmenities();
        String[] amenityNames = amenitiesDisponibles.stream().map(Amenity::getNombre).toArray(String[]::new);

        // Crear el JComboBox para seleccionar el amenity
        amenityDropdown = new JComboBox<>(amenityNames);
        amenityDropdown.setPreferredSize(new Dimension(150, 25)); // Establecer un tamaño preferido
        add(new JLabel("Seleccione un Amenity a leer:"));
        add(amenityDropdown);

        // Botón para leer el amenity
        JButton readButton = new JButton("Leer Amenity");
        readButton.addActionListener(e -> readAmenity());
        add(readButton);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setPreferredSize(new Dimension(250, 100));
        add(new JScrollPane(resultArea));

        // Botón para regresar al panel anterior
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel"));
        add(backButton);
    }

    private void readAmenity() {
        try {
            String selectedAmenityName = (String) amenityDropdown.getSelectedItem();
            Amenity amenity = crudController.getAllAmenities().stream()
                .filter(a -> a.getNombre().equals(selectedAmenityName))
                .findFirst()
                .orElse(null);

            if (amenity != null) {
                resultArea.setText("Amenity encontrado:\n"
                    + "ID: " + amenity.getIdAmenity() + "\n"
                    + "Nombre: " + amenity.getNombre() + "\n"
                    + "Descripción: " + amenity.getDescripcion());
            } else {
                resultArea.setText("Amenity no encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al leer el amenity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

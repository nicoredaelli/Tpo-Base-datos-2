package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;
import org.example.entidades.Zona;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.List;

public class CreatePOIPanel extends JPanel {
    private JTextField nombreField, descripcionField;
    private JComboBox<String> zonaDropdown;
    private CRUDController crudController;
    private List<Zona> zonasDisponibles;

    public CreatePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Inicializar campos de entrada
        nombreField = new JTextField(20);
        descripcionField = new JTextField(50);

        // Obtener la lista de zonas disponibles desde el CRUDController
        zonasDisponibles = crudController.getZonasDisponibles();

        // Crear la lista desplegable con los nombres de las zonas
        zonaDropdown = new JComboBox<>(zonasDisponibles.stream()
                .map(Zona::getNombre)
                .toArray(String[]::new));

        add(new JLabel("Nombre del POI:"));
        add(nombreField);
        add(new JLabel("Descripción del POI:"));
        add(descripcionField);
        add(new JLabel("Zona:"));
        add(zonaDropdown);

        JButton createButton = new JButton("Crear POI");
        createButton.addActionListener(e -> {
            createPOI();
            clearFields();
        });

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel"));

        add(createButton);
        add(backButton);
    }

    private void createPOI() {
        try {
            int idPoi = crudController.getUltimoIdPuntoDeInteres() + 1;

            // Obtener datos ingresados
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();

            // Obtener el ID de la zona seleccionada
            int zonaId = zonasDisponibles.get(zonaDropdown.getSelectedIndex()).getIdZona();

            // Crear el nuevo POI
            PuntoDeInteres nuevoPuntoDeInteres = new PuntoDeInteres(
                    new ObjectId(),
                    idPoi,
                    nombre,
                    descripcion,
                    zonaId
            );

            crudController.createPuntoDeInteres(nuevoPuntoDeInteres);
            JOptionPane.showMessageDialog(this, "Punto de Interés creado exitosamente con ID: " + idPoi);

            // Limpiar los campos después de la creación
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear el POI.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos de texto y restablecer el desplegable
    private void clearFields() {
        nombreField.setText("");
        descripcionField.setText("");
        zonaDropdown.setSelectedIndex(0);
    }
}

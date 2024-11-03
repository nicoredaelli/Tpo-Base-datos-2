package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;

public class UpdatePOIPanel extends JPanel {
    private JTextField idField, nameField, descriptionField, zoneField;
    private JButton loadButton, updateButton, backButton;
    private CRUDController crudController;
    private PuntoDeInteres puntoDeInteres;

    public UpdatePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        zoneField = new JTextField(10);

        add(new JLabel("ID del POI a actualizar:"));
        add(idField);

        loadButton = new JButton("Cargar POI");
        loadButton.addActionListener(e -> cargarPOI());
        add(loadButton);

        add(new JLabel("Nuevo Nombre:"));
        add(nameField);
        add(new JLabel("Nueva Descripción:"));
        add(descriptionField);
        add(new JLabel("ID de la Nueva Zona:"));
        add(zoneField);

        updateButton = new JButton("Actualizar POI");
        updateButton.addActionListener(e -> actualizarPOI());
        updateButton.setEnabled(false); // Se activa solo después de cargar un POI
        add(updateButton);

        backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Ajusta el nombre si es necesario
        add(backButton);
    }

    private void cargarPOI() {
        try {
            int idPoi = Integer.parseInt(idField.getText());
            puntoDeInteres = crudController.readPuntoDeInteres(idPoi);

            if (puntoDeInteres != null) {
                // Mostrar los datos del POI en los campos correspondientes
                nameField.setText(puntoDeInteres.getNombre());
                descriptionField.setText(puntoDeInteres.getDescripcion());
                zoneField.setText(String.valueOf(puntoDeInteres.getZona()));

                // Habilitar el botón de actualización
                updateButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Punto de Interés cargado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el Punto de Interés con el ID: " + idPoi, "Error", JOptionPane.ERROR_MESSAGE);
                updateButton.setEnabled(false); // Deshabilitar si no se encuentra el POI
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPOI() {
       
            int idPoi = Integer.parseInt(idField.getText());
            String nuevoNombre = nameField.getText();
            String nuevaDescripcion = descriptionField.getText();
            int nuevaZona = Integer.parseInt(zoneField.getText());

            

           // Llama al controlador para obtener el POI actualizado
        crudController.updatePuntoDeInteres(idPoi,nuevoNombre,nuevaDescripcion,nuevaZona);

        
    }
}

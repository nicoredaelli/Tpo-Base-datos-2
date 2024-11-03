package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;
import org.bson.types.ObjectId;

public class CreatePOIPanel extends JPanel {
    private JTextField nombreField, descripcionField, zonaIdField;
    private CRUDController crudController;

    public CreatePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Campos de texto para ingresar el nombre, descripción y zona ID del POI
        nombreField = new JTextField(20);
        descripcionField = new JTextField(50);
        zonaIdField = new JTextField(10);

        add(new JLabel("Nombre del POI:"));
        add(nombreField);
        add(new JLabel("Descripción del POI:"));
        add(descripcionField);
        add(new JLabel("ID de la Zona:"));
        add(zonaIdField);

        // Botón para crear el POI
        JButton createButton = new JButton("Crear POI");
        createButton.addActionListener(e -> createPOI());

        // Botón para regresar al panel anterior
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Ajusta el nombre si es necesario

        add(createButton);
        add(backButton);
    }

    private void createPOI() {
        try {
            // Obtener el último ID de POI y generar uno nuevo incrementado
            int idPoi = crudController.getUltimoIdPuntoDeInteres() + 1;

            // Obtener los datos ingresados
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            int zonaId = Integer.parseInt(zonaIdField.getText());

            // Crear el nuevo POI
            PuntoDeInteres nuevoPuntoDeInteres = new PuntoDeInteres(
                    new ObjectId(),   // Genera un nuevo ObjectId
                    idPoi,
                    nombre,
                    descripcion,
                    zonaId
            );

            // Guardar el POI en la base de datos
            crudController.createPuntoDeInteres(nuevoPuntoDeInteres);

            JOptionPane.showMessageDialog(this, "Punto de Interés creado exitosamente con ID: " + idPoi);

            // Limpiar los campos después de la creación
            nombreField.setText("");
            descripcionField.setText("");
            zonaIdField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un valor numérico válido para el ID de la Zona.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


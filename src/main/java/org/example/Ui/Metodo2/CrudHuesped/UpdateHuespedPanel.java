package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;


public class UpdateHuespedPanel extends JPanel {
    private JTextField idField, nombreField, apellidoField, telefonoField, emailField, calleField, numeroField, provinciaField, paisField;
    private JButton loadButton, updateButton;
    private CRUDController crudController;
    private Huesped huesped;

    public UpdateHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        nombreField = new JTextField(20);
        apellidoField = new JTextField(20);
        telefonoField = new JTextField(20);
        emailField = new JTextField(20);
        calleField = new JTextField(20);
        numeroField = new JTextField(5);
        provinciaField = new JTextField(20);
        paisField = new JTextField(20);

        add(new JLabel("ID del Huésped a actualizar:"));
        add(idField);

        loadButton = new JButton("Cargar Huésped");
        loadButton.addActionListener(e -> cargarHuesped());
        add(loadButton);

        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Apellido:"));
        add(apellidoField);
        add(new JLabel("Teléfono:"));
        add(telefonoField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Calle:"));
        add(calleField);
        add(new JLabel("Número:"));
        add(numeroField);
        add(new JLabel("Provincia:"));
        add(provinciaField);
        add(new JLabel("País:"));
        add(paisField);

        updateButton = new JButton("Actualizar Huésped");
        updateButton.addActionListener(e -> actualizarHuesped());
        updateButton.setEnabled(false);
        add(updateButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void cargarHuesped() {
        try {
            int idHuesped = Integer.parseInt(idField.getText());
            huesped = crudController.readHuesped(idHuesped);

            if (huesped != null) {
                nombreField.setText(huesped.getNombre());
                apellidoField.setText(huesped.getApellido());
                telefonoField.setText(huesped.getTelefono());
                emailField.setText(huesped.getEmail());
                calleField.setText(huesped.getDireccion().get("calle"));
                numeroField.setText(huesped.getDireccion().get("numero"));
                provinciaField.setText(huesped.getDireccion().get("provincia"));
                paisField.setText(huesped.getDireccion().get("pais"));

                updateButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Huésped cargado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el Huésped con ID: " + idHuesped, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarHuesped() {
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", calleField.getText());
        direccion.put("numero", numeroField.getText());
        direccion.put("provincia", provinciaField.getText());
        direccion.put("pais", paisField.getText());

        huesped.setNombre(nombreField.getText());
        huesped.setApellido(apellidoField.getText());
        huesped.setTelefono(telefonoField.getText());
        huesped.setEmail(emailField.getText());
        huesped.setDireccion(direccion);

        crudController.updateHuesped(huesped);
        JOptionPane.showMessageDialog(this, "Huésped actualizado exitosamente.");
    }
}


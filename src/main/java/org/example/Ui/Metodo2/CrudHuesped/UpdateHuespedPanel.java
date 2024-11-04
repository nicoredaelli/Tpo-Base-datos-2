package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class UpdateHuespedPanel extends JPanel {
    private JComboBox<String> huespedComboBox; // Menú desplegable para seleccionar huésped
    private JTextField nombreField, apellidoField, telefonoField, emailField;
    private JTextField calleField, numeroField, provinciaField, paisField;
    private JButton loadButton, updateButton;
    private CRUDController crudController;
    private Huesped huesped;

    public UpdateHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        huespedComboBox = new JComboBox<>();
        loadHuespedesDisponibles(); // Cargar huéspedes disponibles en el combo box

        add(new JLabel("Seleccione un Huésped:"));
        add(huespedComboBox);

        loadButton = new JButton("Cargar Huésped");
        loadButton.addActionListener(e -> cargarHuesped());
        add(loadButton);

        add(new JLabel("Nombre:"));
        nombreField = new JTextField(20);
        add(nombreField);
        add(new JLabel("Apellido:"));
        apellidoField = new JTextField(20);
        add(apellidoField);
        add(new JLabel("Teléfono:"));
        telefonoField = new JTextField(20);
        add(telefonoField);
        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField);
        add(new JLabel("Calle:"));
        calleField = new JTextField(20);
        add(calleField);
        add(new JLabel("Número:"));
        numeroField = new JTextField(5);
        add(numeroField);
        add(new JLabel("Provincia:"));
        provinciaField = new JTextField(20);
        add(provinciaField);
        add(new JLabel("País:"));
        paisField = new JTextField(20);
        add(paisField);

        updateButton = new JButton("Actualizar Huésped");
        updateButton.addActionListener(e -> actualizarHuesped());
        updateButton.setEnabled(false);
        add(updateButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void loadHuespedesDisponibles() {
        List<Huesped> huespedes = crudController.getHuespedesDisponibles();
        for (Huesped huesped : huespedes) {
            huespedComboBox.addItem(huesped.getNombre() + " " + huesped.getApellido());
        }
    }

    private void cargarHuesped() {
        String selectedHuesped = (String) huespedComboBox.getSelectedItem();
        if (selectedHuesped != null) {
            String[] parts = selectedHuesped.split(" ");
            String nombre = parts[0];
            String apellido = parts[1];

            huesped = crudController.readHuespedByName(nombre, apellido); // Método que debes implementar

            if (huesped != null) {
                nombreField.setText(huesped.getNombre());
                apellidoField.setText(huesped.getApellido());
                telefonoField.setText(huesped.getTelefono());
                emailField.setText(huesped.getEmail());

                // Recuperar la dirección del mapa
                Map<String, String> direccion = huesped.getDireccion(); // Asegúrate de que este método exista y funcione
                calleField.setText(direccion.getOrDefault("calle", ""));
                numeroField.setText(direccion.getOrDefault("numero", ""));
                provinciaField.setText(direccion.getOrDefault("provincia", ""));
                paisField.setText(direccion.getOrDefault("pais", ""));

                updateButton.setEnabled(true);
                
            } 
        }
    }

    private void actualizarHuesped() {
        Map<String, String> direccion = Map.of(
            "calle", calleField.getText(),
            "numero", numeroField.getText(),
            "provincia", provinciaField.getText(),
            "pais", paisField.getText()
        );

        huesped.setNombre(nombreField.getText());
        huesped.setApellido(apellidoField.getText());
        huesped.setTelefono(telefonoField.getText());
        huesped.setEmail(emailField.getText());
        huesped.setDireccion(direccion);

        crudController.updateHuesped(huesped);
        JOptionPane.showMessageDialog(this, "Huésped actualizado exitosamente.");
    }
}

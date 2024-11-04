package org.example.Ui.Metodo11;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.*;

import java.util.Map;

import org.bson.Document;

public class DetallesHuespedPanel extends JPanel {
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;
    private JComboBox<String> huespedDropdown;
    private JCheckBox nameCheckBox, apellidoCheckBox, telefonoCheckBox, emailCheckBox, direccionCheckBox;
    private JButton loadButton;
    private JTextArea resultArea;

    public DetallesHuespedPanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        this.databaseQueryController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        // Panel superior para seleccionar huésped y atributos
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel dropdownPanel = new JPanel(new FlowLayout());

        dropdownPanel.add(new JLabel("Seleccione Huésped:"));
        huespedDropdown = new JComboBox<>();
        loadHuespedesIntoDropdown(); // Cargar lista de huéspedes en el combo
        dropdownPanel.add(huespedDropdown);

        // Panel para seleccionar atributos a mostrar
        JPanel attributesPanel = new JPanel(new GridLayout(5, 1));
        nameCheckBox = new JCheckBox("Nombre");
        apellidoCheckBox = new JCheckBox("Apellido");
        telefonoCheckBox = new JCheckBox("Teléfono");
        emailCheckBox = new JCheckBox("Email");
        direccionCheckBox = new JCheckBox("Dirección Completa");

        attributesPanel.add(nameCheckBox);
        attributesPanel.add(apellidoCheckBox);
        attributesPanel.add(telefonoCheckBox);
        attributesPanel.add(emailCheckBox);
        attributesPanel.add(direccionCheckBox);

        topPanel.add(dropdownPanel, BorderLayout.NORTH);
        topPanel.add(attributesPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Botón para cargar datos
        loadButton = new JButton("Mostrar datos");
        loadButton.addActionListener(e -> loadHuespedData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(loadButton);

        // Botón para regresar al menú principal
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Área de texto para mostrar resultados
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    // Método para cargar los huéspedes en el JComboBox
    private void loadHuespedesIntoDropdown() {
        List<Huesped> huespedesDisponibles = crudController.getHuespedesDisponiblesNico(); // Asume un método getHuespedes() en CRUDController
        if (huespedesDisponibles != null && !huespedesDisponibles.isEmpty()) {
            for (Huesped huesped : huespedesDisponibles) {
                huespedDropdown.addItem(huesped.getIdHuesped() + " - " + huesped.getNombre() + " " + huesped.getApellido());
            }
        } else {
            huespedDropdown.addItem("No hay huéspedes disponibles");
        }
    }

    private void loadHuespedData() {
        resultArea.setText(""); // Limpiar el área de resultados
        try {
            String selectedHuesped = (String) huespedDropdown.getSelectedItem();
            if (selectedHuesped == null || selectedHuesped.equals("No hay huéspedes disponibles")) {
                resultArea.setText("No se ha seleccionado ningún huésped.");
                return;
            }
            int idHuesped = Integer.parseInt(selectedHuesped.split(" - ")[0]);

            // Obtener datos del huésped como Document
            Document huespedDoc = databaseQueryController.HuespedPorID(idHuesped);
            if (huespedDoc != null) {
                StringBuilder details = new StringBuilder("Detalles del Huésped:\n");

                // Mostrar los datos según los checkboxes seleccionados
                if (nameCheckBox.isSelected() && huespedDoc.containsKey("nombre")) {
                    details.append("Nombre: ").append(huespedDoc.getString("nombre")).append("\n");
                }
                if (apellidoCheckBox.isSelected() && huespedDoc.containsKey("apellido")) {
                    details.append("Apellido: ").append(huespedDoc.getString("apellido")).append("\n");
                }
                if (telefonoCheckBox.isSelected() && huespedDoc.containsKey("telefono")) {
                    details.append("Teléfono: ").append(huespedDoc.getString("telefono")).append("\n");
                }
                if (emailCheckBox.isSelected() && huespedDoc.containsKey("email")) {
                    details.append("Email: ").append(huespedDoc.getString("email")).append("\n");
                }
                if (direccionCheckBox.isSelected() && huespedDoc.containsKey("direccion")) {
                    details.append("Dirección Completa:\n");
                    Map<String, Object> direccion = huespedDoc.get("direccion", Document.class);
                    direccion.forEach((key, value) -> details.append("  ").append(key).append(": ").append(value).append("\n"));
                }

                // Si no se seleccionó ningún campo, muestra un mensaje
                if (details.toString().equals("Detalles del Huésped:\n")) {
                    details.append("No se seleccionó ningún campo para mostrar.");
                }

                resultArea.setText(details.toString());
            } else {
                resultArea.setText("Huésped no encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles del huésped: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


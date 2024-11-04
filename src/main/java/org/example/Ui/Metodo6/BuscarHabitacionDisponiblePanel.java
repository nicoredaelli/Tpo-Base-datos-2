package org.example.Ui.Metodo6;


import org.bson.Document;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.bson.Document;

public class BuscarHabitacionDisponiblePanel extends JPanel {
    private JTextField checkinField;
    private JTextField checkoutField;
    private JTextArea resultArea;
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;

    public BuscarHabitacionDisponiblePanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        this.databaseQueryController = new DatabaseQueryController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Márgenes más pequeños
        
        // Panel de entrada para fechas con GridBagLayout para mayor control de espacio
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre componentes

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        checkinField = new JTextField(8);
        inputPanel.add(checkinField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        checkoutField = new JTextField(8);
        inputPanel.add(checkoutField, gbc);

        // Botón de búsqueda
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> buscarHabitaciones());
        inputPanel.add(searchButton, gbc);

        // Botón de regreso
        gbc.gridy = 3;
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        inputPanel.add(backButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Área de texto para mostrar resultados
        resultArea = new JTextArea(8, 25); // Reducir tamaño
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private void buscarHabitaciones() {
        resultArea.setText(""); // Limpiar el área de resultados
        try {
            Date checkin = parseDate(checkinField.getText());
            Date checkout = parseDate(checkoutField.getText());

            // Llama al CRUDController para obtener habitaciones disponibles
            List<Document> habitacionesDisponibles = databaseQueryController.getHabitacionesDisponibles(checkin, checkout);
            if (habitacionesDisponibles != null && !habitacionesDisponibles.isEmpty()) {
                StringBuilder result = new StringBuilder("Habitaciones disponibles:\n");
                for (Document habitacion : habitacionesDisponibles) {
                    result.append("ID Habitación: ").append(habitacion.getInteger("id_habitacion")).append("\n");
                    result.append("Número: ").append(habitacion.getInteger("nro_habitacion")).append("\n");
                    result.append("Tipo: ").append(habitacion.getString("tipo_habitacion")).append("\n\n");
                }
                resultArea.setText(result.toString());
            } else {
                resultArea.setText("No se encontraron habitaciones disponibles en el rango de fechas indicado.");
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar habitaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Activa validación estricta
        return dateFormat.parse(dateString); // Lanza ParseException si el formato es incorrecto
    }
}



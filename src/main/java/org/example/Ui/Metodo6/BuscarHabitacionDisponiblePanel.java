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

        // Panel de entrada para fechas
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Ingrese Check-in (YYYY-MM-DD):"));
        checkinField = new JTextField(10);
        inputPanel.add(checkinField);

        inputPanel.add(new JLabel("Ingrese Check-out (YYYY-MM-DD):"));
        checkoutField = new JTextField(10);
        inputPanel.add(checkoutField);

        // Botón para buscar habitaciones
        JButton searchButton = new JButton("Buscar Habitaciones");
        searchButton.addActionListener(e -> buscarHabitaciones());
        inputPanel.add(searchButton);

        // Botón para regresar al menú principal
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(10, 30);
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



package org.example.Ui.Metodo8;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;
import org.example.entidades.Huesped;
import org.example.entidades.Reserva;

import javax.swing.*;
import java.awt.*;

public class ReservasConfirmadasPanel extends JPanel {
    private CRUDController crudController;
    private JTextField codReservaField;
    private JTextArea resultArea;

    public ReservasConfirmadasPanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        setLayout(new BorderLayout());

        // Panel de entrada para el código de reserva
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Ingrese el Código de Reserva:"));
        codReservaField = new JTextField(10);
        inputPanel.add(codReservaField);

        // Botón para buscar la reserva
        JButton loadButton = new JButton("Buscar Reserva");
        loadButton.addActionListener(e -> loadReservaData());
        inputPanel.add(loadButton);

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

    private void loadReservaData() {
        resultArea.setText(""); // Limpiar el área de resultados
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            Reserva reserva = crudController.readReserva(codReserva);

            if (reserva != null) {
                StringBuilder result = new StringBuilder("Información de la Reserva:\n");
                result.append("Código de Reserva: ").append(reserva.getCodReserva()).append("\n");
                result.append("Check-in: ").append(reserva.getCheckin()).append("\n");
                result.append("Check-out: ").append(reserva.getCheckout()).append("\n");
                result.append("Tarifa: ").append(reserva.getTarifa()).append("\n");

                // Obtener y mostrar información del hotel
                Hotel hotel = crudController.readHotel(reserva.getIdHotel());
                if (hotel != null) {
                    result.append("\nInformación del Hotel:\n");
                    result.append("  Nombre: ").append(hotel.getNombre()).append("\n");
                    result.append("  Email: ").append(hotel.getEmail()).append("\n");
                } else {
                    result.append("\nHotel no encontrado para ID ").append(reserva.getIdHotel()).append("\n");
                }

                // Obtener y mostrar información de la habitación
                Habitacion habitacion = crudController.readHabitacion(reserva.getIdHabitacion());
                if (habitacion != null) {
                    result.append("\nInformación de la Habitación:\n");
                    result.append("  Número de Habitación: ").append(habitacion.getNroHabitacion()).append("\n");
                    result.append("  Tipo de Habitación: ").append(habitacion.getTipoHabitacion()).append("\n");
                } else {
                    result.append("\nHabitación no encontrada para ID ").append(reserva.getIdHabitacion()).append("\n");
                }

                // Obtener y mostrar información del huésped
                Huesped huesped = crudController.readHuesped(reserva.getIdHuesped());
                if (huesped != null) {
                    result.append("\nInformación del Huésped:\n");
                    result.append("  Nombre: ").append(huesped.getNombre()).append(" ").append(huesped.getApellido()).append("\n");
                    result.append("  Teléfono: ").append(huesped.getTelefono()).append("\n");
                    result.append("  Email: ").append(huesped.getEmail()).append("\n");
                } else {
                    result.append("\nHuésped no encontrado para ID ").append(reserva.getIdHuesped()).append("\n");
                }

                resultArea.setText(result.toString());
            } else {
                resultArea.setText("No se encontró ninguna reserva con el código especificado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



package org.example.Ui.Metodo9;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;
import org.example.entidades.Huesped;
import org.example.entidades.Reserva;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservasPorHuespedPanel extends JPanel {
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;
    private JComboBox<String> huespedDropdown;
    private JTextArea resultArea;

    public ReservasPorHuespedPanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        this.databaseQueryController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        // Panel de entrada para seleccionar el huésped
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Seleccione el Huésped:"));
        huespedDropdown = new JComboBox<>();
        loadHuespedesIntoDropdown();
        inputPanel.add(huespedDropdown);

        // Botón para buscar las reservas
        JButton loadButton = new JButton("Buscar Reservas");
        loadButton.addActionListener(e -> loadReservasData());
        inputPanel.add(loadButton);

        // Botón para regresar al menú principal
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    // Método para cargar los huéspedes en el JComboBox
    private void loadHuespedesIntoDropdown() {
        List<Huesped> huespedesDisponibles = crudController.getHuespedesDisponiblesNico();  // Asume que hay un método getHuespedes() en CRUDController
        if (huespedesDisponibles != null && !huespedesDisponibles.isEmpty()) {
            for (Huesped huesped : huespedesDisponibles) {
                huespedDropdown.addItem(huesped.getIdHuesped() + " - " + huesped.getNombre() + " " + huesped.getApellido());
            }
        } else {
            huespedDropdown.addItem("No hay huéspedes disponibles");
        }
    }

    private void loadReservasData() {
        resultArea.setText(""); // Limpiar el área de resultados
        try {
            String selectedHuesped = (String) huespedDropdown.getSelectedItem();
            if (selectedHuesped == null || selectedHuesped.equals("No hay huéspedes disponibles")) {
                resultArea.setText("No se ha seleccionado ningún huésped.");
                return;
            }
            int idHuesped = Integer.parseInt(selectedHuesped.split(" - ")[0]);

            // Llamar al controlador para obtener todas las reservas del huésped
            List<Reserva> reservas = databaseQueryController.findReservasByHuesped(idHuesped);

            if (!reservas.isEmpty()) {
                StringBuilder result = new StringBuilder("Reservas para el huésped seleccionado:\n\n");
                for (Reserva reserva : reservas) {
                    result.append("Código de Reserva: ").append(reserva.getCodReserva()).append("\n");
                    result.append("Check-in: ").append(reserva.getCheckin()).append("\n");
                    result.append("Check-out: ").append(reserva.getCheckout()).append("\n");
                    result.append("Estado: ").append(reserva.getEstadoReserva()).append("\n");
                    result.append("Tarifa: ").append(reserva.getTarifa()).append("\n");

                    // Información del hotel
                    Hotel hotel = crudController.readHotel(reserva.getIdHotel());
                    if (hotel != null) {
                        result.append("\nInformación del Hotel:\n");
                        result.append("  Nombre: ").append(hotel.getNombre()).append("\n");
                        result.append("  Email: ").append(hotel.getEmail()).append("\n");
                    } else {
                        result.append("\nHotel no encontrado para ID ").append(reserva.getIdHotel()).append("\n");
                    }

                    // Información de la habitación
                    Habitacion habitacion = crudController.readHabitacion(reserva.getIdHabitacion());
                    if (habitacion != null) {
                        result.append("\nInformación de la Habitación:\n");
                        result.append("  Número de Habitación: ").append(habitacion.getNroHabitacion()).append("\n");
                        result.append("  Tipo de Habitación: ").append(habitacion.getTipoHabitacion()).append("\n");
                    } else {
                        result.append("\nHabitación no encontrada para ID ").append(reserva.getIdHabitacion()).append("\n");
                    }

                    result.append("\n--------------------------\n");
                }
                resultArea.setText(result.toString());
            } else {
                resultArea.setText("No se encontraron reservas para el huésped seleccionado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las reservas del huésped: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


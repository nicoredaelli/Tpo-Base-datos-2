package org.example.Ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuPanel extends JPanel {
    public MenuPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crear un panel central con GridLayout para organizar los botones en una cuadrícula
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttonLabels = {
                "Gestión de Hoteles, Habitaciones, Amenities y Puntos de Interés",
                "Gestión de Huéspedes y Reservas",
                "Hoteles Cercanos a Puntos de Interés",
                "Consulta de Información de Hotel",
                "Puntos de Interés Cercanos al Hotel",
                "Disponibilidad de Habitaciones por Rango de Fechas",
                "Amenities de la Habitación",
                "Búsqueda de Reservas por Número de Confirmación",
                "Reservas por Huésped",
                "Reservas por Fecha en el Hotel",
                "Detalles del Huésped"
        };

        ActionListener actionListener = e -> {
            String command = e.getActionCommand();
            switch (command) {
                case "Gestión de Hoteles, Habitaciones, Amenities y Puntos de Interés":
                    mainFrame.showPanel("GestiónHHAP");
                    break;
                case "Gestión de Huéspedes y Reservas":
                    mainFrame.showPanel("GestiónHR");
                    break;
                case "Consulta de Información de Hotel":
                    mainFrame.showPanel("InfoHotelPanel");
                    break;
                case "Disponibilidad de Habitaciones por Rango de Fechas":
                    mainFrame.showPanel("BuscarHabitacionDisponiblePanel");
                    break;
                case "Búsqueda de Reservas por Número de Confirmación":
                    mainFrame.showPanel("ReservasConfirmadasPanel");
                    break;
                case "Reservas por Huésped":
                    mainFrame.showPanel("ReservasPorHuespedPanel");
                    break;
                case "Detalles del Huésped":
                    mainFrame.showPanel("DetallesHuespedPanel");
                    break;
                case "Reservas por Fecha en el Hotel":
                    mainFrame.showPanel("ReservasPorFechaPanel");
                    break;
                case "Hoteles Cercanos a Puntos de Interés":
                    mainFrame.showPanel("HotelesCercanosPOI");
                    break;
                case "Puntos de Interés Cercanos al Hotel":
                    mainFrame.showPanel("PuntosInteresCercanosHotel");
                    break;
                case "Amenities de la Habitación":
                    mainFrame.showPanel("AmenitiesHabitacion");
                    break;
                default:
                    System.out.println("Botón no implementado: " + command);
                    break;
            }
        };

        // Crear y añadir los botones al panel de botones
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(actionListener);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(300, 30)); // Ajustar tamaño de los botones
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER); // Añadir el panel de botones al centro del panel principal
    }
}

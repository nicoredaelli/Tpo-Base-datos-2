package org.example.Ui;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    public MenuPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
                    mainFrame.showPanel("GestiónHHAP"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Gestión de Huéspedes y Reservas":
                    mainFrame.showPanel("GestiónHR"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Consulta de Información de Hotel":
                    mainFrame.showPanel("InfoHotelPanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Disponibilidad de Habitaciones por Rango de Fechas":
                    mainFrame.showPanel("BuscarHabitacionDisponiblePanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Búsqueda de Reservas por Número de Confirmación":
                    mainFrame.showPanel("ReservasConfirmadasPanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Reservas por Huésped":
                    mainFrame.showPanel("ReservasPorHuespedPanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Detalles del Huésped":
                    mainFrame.showPanel("DetallesHuespedPanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                case "Reservas por Fecha en el Hotel":
                    mainFrame.showPanel("ReservasPorFechaPanel"); // Este nombre debe coincidir con el panel agregado
                    break;
                // Agrega más casos según los botones restantes
                default:
                    System.out.println("Botón no implementado: " + command);
                    break;
            }
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(actionListener);
            add(button);
        }
    }
}
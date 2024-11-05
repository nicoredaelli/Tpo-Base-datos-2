package org.example.Ui.Metodo2;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;

public class GestiónHR extends JPanel {
    public GestiónHR(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crear un panel central con GridLayout para organizar los botones en una columna
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnHuesped = new JButton("CRUD Huesped");
        btnHuesped.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));

        JButton btnReserva = new JButton("CRUD Reservas");
        btnReserva.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));

        // Botón de regresar
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));

        // Añadir los botones al panel de botones
        buttonPanel.add(btnHuesped);
        buttonPanel.add(btnReserva);
        buttonPanel.add(backButton);

        // Añadir el panel de botones al centro del panel principal
        add(buttonPanel, BorderLayout.CENTER);
    }
}

package org.example.Ui.Metodo1;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;

public class GestiónHHAP extends JPanel {
    public GestiónHHAP(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crear un panel central con GridLayout para organizar los botones en una columna
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnHotel = new JButton("CRUD Hotel");
        btnHotel.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        JButton btnRoom = new JButton("CRUD Habitaciones");
        btnRoom.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        JButton btnAmenity = new JButton("CRUD Amenities");
        btnAmenity.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel"));

        JButton btnPOI = new JButton("CRUD POI");
        btnPOI.addActionListener(e -> mainFrame.showPanel("POICRUDPanel"));

        // Botón de regresar
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));

        // Añadir los botones al panel de botones
        buttonPanel.add(btnHotel);
        buttonPanel.add(btnRoom);
        buttonPanel.add(btnAmenity);
        buttonPanel.add(btnPOI);
        buttonPanel.add(backButton);

        // Añadir el panel de botones al centro del panel principal
        add(buttonPanel, BorderLayout.CENTER);
    }
}

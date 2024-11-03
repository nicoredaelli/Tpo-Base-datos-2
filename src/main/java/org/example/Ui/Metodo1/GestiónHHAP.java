package org.example.Ui.Metodo1;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.Ui.MainFrame;

public class GestiónHHAP extends JPanel {
    public GestiónHHAP(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnHotel = new JButton("CRUD Hotel");
        btnHotel.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel")); // Cambia el nombre aquí

        JButton btnRoom = new JButton("CRUD Habitaciones");
        btnRoom.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel")); // Verifica si este panel está definido

        JButton btnAmenity = new JButton("CRUD Amenities");
        btnAmenity.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel")); // Verifica si este panel está definido

        JButton btnPOI = new JButton("CRUD POI");
        btnPOI.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Verifica si este panel está definido
        
        // Agregar el botón de regresar
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu")); // Asegúrate de que el nombre del panel sea correcto

        add(btnHotel);
        add(btnRoom);
        add(btnAmenity);
        add(btnPOI);
        
        add(backButton);
    }
}

package org.example.Ui.Metodo2;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.Ui.MainFrame;

public class GestiónHR extends JPanel {
    public GestiónHR(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnHuesped = new JButton("CRUD Huesped");
        btnHuesped.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel")); // Cambia el nombre aquí

        JButton btnReserva = new JButton("CRUD Reservas");
        btnReserva.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel")); // Verifica si este panel está definido

        
        // Agregar el botón de regresar
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu")); // Asegúrate de que el nombre del panel sea correcto

        add(btnHuesped);
        add(btnReserva);
        
        
        add(backButton);
    }
}

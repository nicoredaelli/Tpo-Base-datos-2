package org.example.Ui.Metodo5;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Hotel;
import org.example.entidades.PuntoDeInteres;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PuntosInteresCercanosHotelPanel extends JPanel {
    private MainFrame mainFrame;
    private DatabaseQueryController dbController;
    private JComboBox<String> hotelComboBox; // Mostrar solo el nombre
    private JTextArea puntosInteresTextArea;

    public PuntosInteresCercanosHotelPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dbController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        // Obtener la lista de hoteles y configurar el combo box para mostrar solo los nombres
        List<Hotel> hoteles = dbController.getHotelesDisponibles();
        hotelComboBox = new JComboBox<>(hoteles.stream().map(Hotel::getNombre).toArray(String[]::new));

        JButton searchButton = new JButton("Buscar Puntos de Interés");
        JButton backButton = new JButton("Regresar");

        searchButton.addActionListener(e -> {
            int selectedIndex = hotelComboBox.getSelectedIndex();
            if (selectedIndex != -1) {
                Hotel selectedHotel = hoteles.get(selectedIndex);
                List<PuntoDeInteres> puntosDeInteres = dbController.getPOIsByIDHotel(selectedHotel.getIdHotel());
                displayPuntosDeInteres(puntosDeInteres);
            }
        });

        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.add(new JLabel("Hotel:"));
        inputPanel.add(hotelComboBox);
        inputPanel.add(searchButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        // Ajustar el tamaño del área de texto para hacer la pantalla más pequeña
        puntosInteresTextArea = new JTextArea(8, 25);
        puntosInteresTextArea.setEditable(false);
        add(new JScrollPane(puntosInteresTextArea), BorderLayout.CENTER);
    }

    private void displayPuntosDeInteres(List<PuntoDeInteres> puntosDeInteres) {
        StringBuilder puntosDeInteresInfo = new StringBuilder();
        for (PuntoDeInteres poi : puntosDeInteres) {
            puntosDeInteresInfo.append(poi.getNombre()).append("\n");
        }
        puntosInteresTextArea.setText(puntosDeInteresInfo.toString());
    }
}

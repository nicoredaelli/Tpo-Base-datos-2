package org.example.Ui.Metodo5;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Hotel;
import org.example.entidades.PuntoDeInteres;

public class PuntosInteresCercanosHotelPanel extends JPanel {
    private MainFrame mainFrame;
    private DatabaseQueryController dbController;
    private JComboBox<Hotel> hotelComboBox;
    private JTextArea puntosInteresTextArea;

    public PuntosInteresCercanosHotelPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dbController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        List<Hotel> hoteles = dbController.getHotelesDisponibles();
        hotelComboBox = new JComboBox<>(hoteles.toArray(new Hotel[0]));

        JButton searchButton = new JButton("Buscar Puntos de Interés");
        JButton backButton = new JButton("Regresar");

        searchButton.addActionListener(e -> {
            Hotel selectedHotel = (Hotel) hotelComboBox.getSelectedItem();
            if (selectedHotel != null) {
                List<PuntoDeInteres> puntosDeInteres = dbController.getPOIsByIDHotel(selectedHotel.getIdHotel());
                displayPuntosDeInteres(puntosDeInteres);
            }
        });

        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Hotel:"));
        inputPanel.add(hotelComboBox);
        inputPanel.add(searchButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        puntosInteresTextArea = new JTextArea(10, 30);
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

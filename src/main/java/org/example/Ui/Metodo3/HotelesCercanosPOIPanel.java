package org.example.Ui.Metodo3;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Hotel;
import org.example.entidades.PuntoDeInteres;

public class HotelesCercanosPOIPanel extends JPanel {
    private MainFrame mainFrame;
    private DatabaseQueryController dbController;
    private JComboBox<PuntoDeInteres> poiComboBox;

    public HotelesCercanosPOIPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dbController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        List<PuntoDeInteres> puntosDeInteres = dbController.getAllPuntosDeInteres();
        poiComboBox = new JComboBox<>(puntosDeInteres.toArray(new PuntoDeInteres[0]));

        JButton searchButton = new JButton("Buscar Hoteles");
        JButton backButton = new JButton("Regresar");

        searchButton.addActionListener(e -> {
            PuntoDeInteres selectedPoi = (PuntoDeInteres) poiComboBox.getSelectedItem();
            if (selectedPoi != null) {
                List<Hotel> hoteles = dbController.getHotelesCercanosAPOI(selectedPoi.getIdPoi());
                displayHoteles(hoteles);
            }
        });

        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Punto de Interés:"));
        inputPanel.add(poiComboBox);
        inputPanel.add(searchButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void displayHoteles(List<Hotel> hoteles) {
        removeAll(); // Elimina todos los componentes del panel antes de agregar los nuevos resultados.
    
        JPanel hotelesPanel = new JPanel();
        hotelesPanel.setLayout(new BoxLayout(hotelesPanel, BoxLayout.Y_AXIS));
    
        if (hoteles.isEmpty()) {
            hotelesPanel.add(new JLabel("No se encontraron hoteles cercanos."));
        } else {
            for (Hotel hotel : hoteles) {
                hotelesPanel.add(new JLabel(hotel.getNombre()));
            }
        }
    
        add(new JScrollPane(hotelesPanel), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
}

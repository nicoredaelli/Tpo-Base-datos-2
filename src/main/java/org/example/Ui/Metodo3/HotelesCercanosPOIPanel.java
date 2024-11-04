package org.example.Ui.Metodo3;

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

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HotelesCercanosPOIPanel extends JPanel {
    private MainFrame mainFrame;
    private DatabaseQueryController dbController;
    private JComboBox<PuntoDeInteres> poiComboBox;
    private JTextArea hotelesTextArea;

    public HotelesCercanosPOIPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dbController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        List<PuntoDeInteres> puntosDeInteres = dbController.getAllPuntosDeInteres();
        
        // Crear el JComboBox con los puntos de interés
        poiComboBox = new JComboBox<>(puntosDeInteres.toArray(new PuntoDeInteres[0]));
        poiComboBox.setRenderer(new POIRenderer()); // Usar un renderizador personalizado
        poiComboBox.setPreferredSize(new Dimension(150, 25)); // Ajustar tamaño del JComboBox

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

        // Usar GridLayout para alinear elementos
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Punto de Interés:"));
        inputPanel.add(poiComboBox);
        inputPanel.add(searchButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        hotelesTextArea = new JTextArea(10, 30); // Ajustar tamaño del JTextArea
        hotelesTextArea.setEditable(false);
        hotelesTextArea.setLineWrap(true);
        hotelesTextArea.setWrapStyleWord(true);
        add(new JScrollPane(hotelesTextArea), BorderLayout.CENTER);
    }

    private void displayHoteles(List<Hotel> hoteles) {
        StringBuilder hotelesInfo = new StringBuilder();
        for (Hotel hotel : hoteles) {
            hotelesInfo.append(hotel.getNombre()).append("\n");
        }
        hotelesTextArea.setText(hotelesInfo.toString());
    }

    // Clase interna para renderizar los elementos del JComboBox
    private class POIRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof PuntoDeInteres) {
                PuntoDeInteres poi = (PuntoDeInteres) value;
                setText(poi.getNombre()); // Mostrar solo el nombre en el JComboBox
            }
            return this;
        }
    }
}


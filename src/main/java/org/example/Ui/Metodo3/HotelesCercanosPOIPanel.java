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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior para selección de POI y botones
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Buscar Hoteles Cercanos"));
        inputPanel.setBackground(new Color(230, 240, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        List<PuntoDeInteres> puntosDeInteres = dbController.getAllPuntosDeInteres();
        
        // Crear el JComboBox con los puntos de interés
        poiComboBox = new JComboBox<>(puntosDeInteres.toArray(new PuntoDeInteres[0]));
        
        JButton searchButton = new JButton("Buscar Hoteles");
        JButton backButton = new JButton("Regresar");

        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setFocusPainted(false);

        // Layout para los elementos del panel superior
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Punto de Interés:"), gbc);
        
        gbc.gridx = 1;
        inputPanel.add(poiComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(searchButton, gbc);
        
        gbc.gridy = 2;
        inputPanel.add(backButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // TextArea para mostrar los hoteles
        hotelesTextArea = new JTextArea(10, 30);
        hotelesTextArea.setEditable(false);
        hotelesTextArea.setLineWrap(true);
        hotelesTextArea.setWrapStyleWord(true);
        hotelesTextArea.setBorder(BorderFactory.createTitledBorder("Hoteles Cercanos"));
        add(new JScrollPane(hotelesTextArea), BorderLayout.CENTER);
        
        // Acción del botón de búsqueda
        searchButton.addActionListener(e -> {
            PuntoDeInteres selectedPoi = (PuntoDeInteres) poiComboBox.getSelectedItem();
            if (selectedPoi != null) {
                List<Hotel> hoteles = dbController.getHotelesCercanosAPOI(selectedPoi.getIdPoi());
                displayHoteles(hoteles);
            }
        });

        // Acción del botón de regreso
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
    }

    private void displayHoteles(List<Hotel> hoteles) {
        hotelesTextArea.setText(""); // Limpiar el área de texto antes de mostrar nuevos resultados.
        
        if (hoteles.isEmpty()) {
            hotelesTextArea.setText("No se encontraron hoteles cercanos.");
        } else {
            StringBuilder hotelesInfo = new StringBuilder();
            for (Hotel hotel : hoteles) {
                hotelesInfo.append(hotel.getNombre()).append("\n");
            }
            hotelesTextArea.setText(hotelesInfo.toString());
        }
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


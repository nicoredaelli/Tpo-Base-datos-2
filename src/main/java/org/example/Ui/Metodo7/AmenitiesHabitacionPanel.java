package org.example.Ui.Metodo7;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Habitacion;
import org.example.entidades.Amenity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AmenitiesHabitacionPanel extends JPanel {
    private MainFrame mainFrame;
    private DatabaseQueryController dbController;
    private JComboBox<Habitacion> habitacionComboBox;
    private JTextArea amenitiesTextArea;

    public AmenitiesHabitacionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dbController = new DatabaseQueryController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de selección de habitación y botones
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Buscar Amenities de Habitación"));
        inputPanel.setBackground(new Color(230, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Recuperar habitaciones disponibles
        List<Habitacion> habitaciones = dbController.getHabitacionesDisponibles();
        habitacionComboBox = new JComboBox<>(habitaciones.toArray(new Habitacion[0]));
        
        JButton searchButton = new JButton("Buscar Amenities");
        JButton backButton = new JButton("Regresar");

        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        backButton.setBackground(new Color(200, 200, 200));
        backButton.setFocusPainted(false);

        // Layout de los elementos en el panel superior
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Habitación:"), gbc);
        
        gbc.gridx = 1;
        inputPanel.add(habitacionComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(searchButton, gbc);
        
        gbc.gridy = 2;
        inputPanel.add(backButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // TextArea para mostrar los amenities
        amenitiesTextArea = new JTextArea(10, 30);
        amenitiesTextArea.setEditable(false);
        amenitiesTextArea.setLineWrap(true);
        amenitiesTextArea.setWrapStyleWord(true);
        amenitiesTextArea.setBorder(BorderFactory.createTitledBorder("Amenities"));
        add(new JScrollPane(amenitiesTextArea), BorderLayout.CENTER);

        // Acción del botón de búsqueda
        searchButton.addActionListener(e -> {
            Habitacion selectedHabitacion = (Habitacion) habitacionComboBox.getSelectedItem();
            if (selectedHabitacion != null) {
                List<Amenity> amenities = dbController.getAmenitiesByHabitacion(selectedHabitacion.getNroHabitacion());
                displayAmenities(amenities);
            }
        });

        // Acción del botón de regreso
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
    }

    private void displayAmenities(List<Amenity> amenities) {
        amenitiesTextArea.setText(""); // Limpiar el área de texto antes de mostrar nuevos resultados.
        
        if (amenities.isEmpty()) {
            amenitiesTextArea.setText("No se encontraron amenities para esta habitación.");
        } else {
            StringBuilder amenitiesInfo = new StringBuilder();
            for (Amenity amenity : amenities) {
                amenitiesInfo.append(amenity.getNombre()).append("\n");
            }
            amenitiesTextArea.setText(amenitiesInfo.toString());
        }
    }
}

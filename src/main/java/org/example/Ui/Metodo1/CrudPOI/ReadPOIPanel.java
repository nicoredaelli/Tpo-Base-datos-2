package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import java.util.List;
public class ReadPOIPanel extends JPanel {
    private JComboBox<String> poiDropdown;
    private JTextArea resultArea;
    private CRUDController crudController;

    public ReadPOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de puntos de interés disponibles desde el CRUDController
        List<PuntoDeInteres> puntosDeInteresDisponibles = crudController.getPuntosDeInteresDisponibles();
        String[] poiNames = new String[puntosDeInteresDisponibles.size()];
        for (int i = 0; i < puntosDeInteresDisponibles.size(); i++) {
            poiNames[i] = puntosDeInteresDisponibles.get(i).getNombre(); // Cambiar para mostrar el nombre
        }

        // Crear el JComboBox para seleccionar el punto de interés
        poiDropdown = new JComboBox<>(poiNames);
        poiDropdown.setPreferredSize(new Dimension(150, 25));
        add(new JLabel("Seleccione un Punto de Interés:"));
        add(poiDropdown);

        // Botón para leer el POI
        JButton readButton = new JButton("Leer POI");
        readButton.addActionListener(e -> readPOI());
        add(readButton);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        // Botón para regresar al panel anterior
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel"));
        add(backButton);
    }

    private void readPOI() {
        String selectedPOIName = (String) poiDropdown.getSelectedItem();
        PuntoDeInteres puntoDeInteres = crudController.getPuntosDeInteresDisponibles().stream()
            .filter(p -> p.getNombre().equals(selectedPOIName))
            .findFirst()
            .orElse(null);

        if (puntoDeInteres != null) {
            resultArea.setText("Punto de Interés encontrado:\n"
                + "ID: " + puntoDeInteres.getIdPoi() + "\n"
                + "Nombre: " + puntoDeInteres.getNombre() + "\n"
                + "Descripción: " + puntoDeInteres.getDescripcion());
        } else {
            resultArea.setText("Punto de interés no encontrado.");
        }
    }
}

package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;
import org.example.entidades.Zona;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdatePOIPanel extends JPanel {
    private JComboBox<String> poiDropdown; // Desplegable para seleccionar el POI
    private JTextField nameField, descriptionField;
    private JComboBox<String> zoneDropdown; // Desplegable para seleccionar la zona
    private JButton loadButton, updateButton, backButton;
    private CRUDController crudController;
    private PuntoDeInteres puntoDeInteres;

    public UpdatePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener lista de puntos de interés disponibles
        List<PuntoDeInteres> puntosDeInteresDisponibles = crudController.getPuntosDeInteresDisponibles();
        String[] poiNames = puntosDeInteresDisponibles.stream()
            .map(PuntoDeInteres::getNombre)
            .toArray(String[]::new);

        // Crear JComboBox para seleccionar el POI
        poiDropdown = new JComboBox<>(poiNames);
        add(new JLabel("Seleccione un POI:"));
        add(poiDropdown);

        nameField = new JTextField(20);
        descriptionField = new JTextField(20);

        // Obtener lista de zonas disponibles y crear JComboBox para zonas
        List<Zona> zonasDisponibles = crudController.getZonasDisponibles();
        String[] zonaNames = zonasDisponibles.stream()
            .map(Zona::getNombre)
            .toArray(String[]::new);
        zoneDropdown = new JComboBox<>(zonaNames);

        add(new JLabel("Nuevo Nombre:"));
        add(nameField);
        add(new JLabel("Nueva Descripción:"));
        add(descriptionField);
        add(new JLabel("Zona:"));
        add(zoneDropdown);

        loadButton = new JButton("Cargar POI");
        loadButton.addActionListener(e -> cargarPOI());
        add(loadButton);

        updateButton = new JButton("Actualizar POI");
        updateButton.addActionListener(e -> actualizarPOI());
        updateButton.setEnabled(false); // Solo se activa tras cargar un POI
        add(updateButton);

        backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel"));
        add(backButton);
    }

    private void cargarPOI() {
        String selectedPOIName = (String) poiDropdown.getSelectedItem();
        PuntoDeInteres selectedPOI = crudController.getPuntosDeInteresDisponibles().stream()
            .filter(p -> p.getNombre().equals(selectedPOIName))
            .findFirst()
            .orElse(null);

        if (selectedPOI != null) {
            puntoDeInteres = selectedPOI;
            nameField.setText(puntoDeInteres.getNombre());
            descriptionField.setText(puntoDeInteres.getDescripcion());

            String zonaNombre = crudController.getZonasDisponibles().stream()
                .filter(z -> z.getIdZona() == puntoDeInteres.getZona())
                .map(Zona::getNombre)
                .findFirst()
                .orElse("");
            zoneDropdown.setSelectedItem(zonaNombre);

            updateButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Punto de Interés cargado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el Punto de Interés seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            updateButton.setEnabled(false);
        }
    }

    private void actualizarPOI() {
        if (puntoDeInteres == null) {
            JOptionPane.showMessageDialog(this, "No se ha cargado un Punto de Interés.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = nameField.getText();
        String nuevaDescripcion = descriptionField.getText();
        String selectedZone = (String) zoneDropdown.getSelectedItem();
        int zonaId = crudController.getZonasDisponibles().stream()
            .filter(z -> z.getNombre().equals(selectedZone))
            .map(Zona::getIdZona)
            .findFirst()
            .orElse(-1);

        if (zonaId != -1) {
            crudController.updatePuntoDeInteres(puntoDeInteres.getIdPoi(), nuevoNombre, nuevaDescripcion, zonaId);
            JOptionPane.showMessageDialog(this, "Punto de Interés actualizado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Zona seleccionada inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package org.example.Ui.Metodo1.CrudPOI;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;
public class DeletePOIPanel extends JPanel {
    private JComboBox<String> poiDropdown; // Usar JComboBox para los nombres
    private JButton deleteButton, backButton;
    private CRUDController crudController;

    public DeletePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de puntos de interés disponibles desde el CRUDController
        List<PuntoDeInteres> puntosDeInteresDisponibles = crudController.getPuntosDeInteresDisponibles();
        
        // Crear un array para almacenar los nombres de los POIs
        String[] poiNames = new String[puntosDeInteresDisponibles.size()];
        for (int i = 0; i < puntosDeInteresDisponibles.size(); i++) {
            poiNames[i] = puntosDeInteresDisponibles.get(i).getNombre(); // Obtener el nombre de cada POI
        }

        // Crear el JComboBox para seleccionar el POI
        poiDropdown = new JComboBox<>(poiNames);
        poiDropdown.setPreferredSize(new Dimension(150, 25)); // Establecer un tamaño preferido
        add(new JLabel("Seleccione el Punto de Interés a eliminar:"));
        add(poiDropdown);

        deleteButton = new JButton("Eliminar POI");
        deleteButton.addActionListener(e -> eliminarPOI());
        add(deleteButton);

        backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Ajusta el nombre del panel principal si es necesario
        add(backButton);
    }

    private void eliminarPOI() {
        try {
            String selectedPOIName = (String) poiDropdown.getSelectedItem();
            PuntoDeInteres puntoDeInteres = crudController.getPuntosDeInteresDisponibles().stream()
                .filter(p -> p.getNombre().equals(selectedPOIName))
                .findFirst()
                .orElse(null);

            if (puntoDeInteres != null) {
                // Llamada al controlador para eliminar el POI
                crudController.deletePuntoDeInteres(puntoDeInteres.getIdPoi());
                JOptionPane.showMessageDialog(this, "Punto de Interés eliminado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el Punto de Interés seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el punto de interés.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

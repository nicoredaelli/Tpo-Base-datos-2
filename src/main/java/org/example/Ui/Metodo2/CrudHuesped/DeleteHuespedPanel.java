package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;
import javax.swing.*;
import java.awt.*;
import java.util.List;
public class DeleteHuespedPanel extends JPanel {
    private JComboBox<String> huespedDropdown; // Menú desplegable para seleccionar huésped
    private CRUDController crudController;

    public DeleteHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de huéspedes disponibles desde el CRUDController
        List<Huesped> huespedesDisponibles = crudController.getHuespedesDisponibles();
        String[] huespedNames = huespedesDisponibles.stream()
            .map(h -> h.getNombre() + " " + h.getApellido())
            .toArray(String[]::new);

        // Crear el JComboBox para seleccionar el huésped
        huespedDropdown = new JComboBox<>(huespedNames);
        huespedDropdown.setPreferredSize(new Dimension(150, 25)); // Establecer un tamaño preferido
        add(new JLabel("Seleccione un Huésped a eliminar:"));
        add(huespedDropdown);

        // Botón para eliminar el huésped
        JButton deleteButton = new JButton("Eliminar Huésped");
        deleteButton.addActionListener(e -> eliminarHuesped());
        add(deleteButton);

        // Botón para regresar al panel anterior
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void eliminarHuesped() {
        try {
            String selectedHuespedName = (String) huespedDropdown.getSelectedItem();
            // Obtener el nombre y apellido del huésped seleccionado
            String[] parts = selectedHuespedName.split(" ");
            String nombre = parts[0];
            String apellido = parts[1];

            // Buscar el huésped correspondiente
            Huesped huesped = crudController.getHuespedesDisponibles().stream()
                .filter(h -> h.getNombre().equals(nombre) && h.getApellido().equals(apellido))
                .findFirst()
                .orElse(null);

            if (huesped != null) {
                // Eliminar al huésped
                crudController.deleteHuesped(huesped.getIdHuesped());
                JOptionPane.showMessageDialog(this, "Huésped eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Huésped no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

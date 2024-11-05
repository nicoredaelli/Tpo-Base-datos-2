package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;

import java.util.List;

public class DeleteHuespedPanel extends JPanel {
    private JComboBox<String> huespedDropdown; // Menú desplegable para seleccionar huésped
    private CRUDController crudController;

    public DeleteHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir márgenes

        JLabel titleLabel = new JLabel("Seleccione un Huésped a eliminar:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear el JComboBox para seleccionar el huésped
        huespedDropdown = new JComboBox<>();
        huespedDropdown.setMaximumSize(new Dimension(250, 30)); // Ajustar el tamaño del combo box
        loadHuespedes();

        JButton deleteButton = new JButton("Eliminar Huésped");
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(e -> eliminarHuesped());

        JButton backButton = new JButton("Regresar");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // Espacio entre componentes
        contentPanel.add(huespedDropdown);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(deleteButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(backButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadHuespedes() {
        // Cargar la lista de huéspedes en el JComboBox
        List<Huesped> huespedesDisponibles = crudController.getAllHuespedes();
        for (Huesped huesped : huespedesDisponibles) {
            huespedDropdown.addItem(huesped.getNombre() + " " + huesped.getApellido());
        }
    }

    private void eliminarHuesped() {
        try {
            String selectedHuespedName = (String) huespedDropdown.getSelectedItem();
            if (selectedHuespedName != null) {
                // Obtener el nombre y apellido del huésped seleccionado
                String[] parts = selectedHuespedName.split(" ");
                String nombre = parts[0];
                String apellido = parts[1];

                // Buscar el huésped correspondiente
                Huesped huesped = crudController.getAllHuespedes().stream()
                        .filter(h -> h.getNombre().equals(nombre) && h.getApellido().equals(apellido))
                        .findFirst()
                        .orElse(null);

                if (huesped != null) {
                    // Eliminar al huésped
                    crudController.deleteHuesped(huesped.getIdHuesped());
                    JOptionPane.showMessageDialog(this, "Huésped eliminado exitosamente.");
                    loadHuespedes(); // Recargar la lista después de la eliminación
                } else {
                    JOptionPane.showMessageDialog(this, "Huésped no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

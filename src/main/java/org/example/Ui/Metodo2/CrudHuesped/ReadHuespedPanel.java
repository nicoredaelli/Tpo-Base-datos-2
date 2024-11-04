package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;




import java.util.List;

import java.awt.*;

public class ReadHuespedPanel extends JPanel {
    private JComboBox<String> huespedDropdown; // Menú desplegable para seleccionar huésped
    private JTextArea resultArea; // Área de texto para mostrar la información del huésped
    private CRUDController crudController;

    public ReadHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de huéspedes disponibles desde el CRUDController
        List<Huesped> huespedesDisponibles = crudController.getAllHuespedes();
        String[] huespedNames = huespedesDisponibles.stream()
            .map(h -> h.getNombre() + " " + h.getApellido())
            .toArray(String[]::new);

        // Crear el JComboBox para seleccionar el huésped
        huespedDropdown = new JComboBox<>(huespedNames);
        huespedDropdown.setPreferredSize(new Dimension(150, 25)); // Establecer un tamaño preferido
        add(new JLabel("Seleccione un Huésped a leer:"));
        add(huespedDropdown);

        // Botón para leer el huésped
        JButton readButton = new JButton("Leer Huésped");
        readButton.addActionListener(e -> readHuesped());
        add(readButton);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setPreferredSize(new Dimension(250, 100));
        add(new JScrollPane(resultArea));

        // Botón para regresar al panel anterior
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void readHuesped() {
        try {
            String selectedHuespedName = (String) huespedDropdown.getSelectedItem();
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
                // Mostrar la información del huésped en el área de texto
                resultArea.setText("Huésped encontrado:\n"
                    + "ID: " + huesped.getIdHuesped() + "\n"
                    + "Nombre: " + huesped.getNombre() + "\n"
                    + "Apellido: " + huesped.getApellido() + "\n"
                    + "Teléfono: " + huesped.getTelefono() + "\n"
                    + "Email: " + huesped.getEmail() + "\n"
                    + "Dirección:\n"
                    + "Calle: " + huesped.getDireccion().get("calle") + "\n"
                    + "Número: " + huesped.getDireccion().get("numero") + "\n"
                    + "Provincia: " + huesped.getDireccion().get("provincia") + "\n"
                    + "País: " + huesped.getDireccion().get("pais"));
            } else {
                resultArea.setText("Huésped no encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al leer el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

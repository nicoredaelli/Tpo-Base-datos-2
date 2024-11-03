package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.PuntoDeInteres;




public class ReadPOIPanel extends JPanel {
    private JTextField idField;
    private JTextArea resultArea;
    private CRUDController crudController;

    public ReadPOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false); // Solo lectura

        add(new JLabel("ID del POI:"));
        add(idField);

        JButton readButton = new JButton("Leer POI");
        readButton.addActionListener(e -> readPOI());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Ajusta el nombre si es necesario

        add(readButton);
        add(backButton);
        add(new JScrollPane(resultArea)); // Para ver el contenido del POI en un área de texto desplazable
    }

    private void readPOI() {
        try {
            int idPoi = Integer.parseInt(idField.getText());
            
            // Leer el POI desde la base de datos
            PuntoDeInteres puntoDeInteres = crudController.readPuntoDeInteres(idPoi);

            if (puntoDeInteres != null) {
                resultArea.setText("Punto de Interés encontrado:\n" + puntoDeInteres.toString());
            } else {
                resultArea.setText("No se encontró el punto de interés con el ID: " + idPoi);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un valor numérico válido para el ID del POI.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

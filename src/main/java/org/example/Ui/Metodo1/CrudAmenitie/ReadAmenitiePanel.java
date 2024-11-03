package org.example.Ui.Metodo1.CrudAmenitie;



import javax.swing.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
import java.awt.Dimension;

public class ReadAmenitiePanel extends JPanel {
    private JTextField idField;
    private JTextArea resultArea;
    private CRUDController crudController;

    public ReadAmenitiePanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Campo para ingresar el ID del amenity
        idField = new JTextField(10);
        add(new JLabel("ID del Amenity a leer:"));
        add(idField);

        // Botón para leer el amenity
        JButton readButton = new JButton("Leer Amenity");
        readButton.addActionListener(e -> readAmenity());
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
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel"));
        add(backButton);
    }

    private void readAmenity() {
        try {
            int idAmenity = Integer.parseInt(idField.getText());
            Amenity amenity = crudController.readAmenity(idAmenity);
            
            if (amenity != null) {
                resultArea.setText("Amenity encontrado:\n"
                    + "ID: " + amenity.getIdAmenity() + "\n"
                    + "Nombre: " + amenity.getNombre() + "\n"
                    + "Descripción: " + amenity.getDescripcion());
            } else {
                resultArea.setText("Amenity no encontrado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
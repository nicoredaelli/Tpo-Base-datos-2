package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;



public class ReadHuespedPanel extends JPanel {
    private JTextField idField;
    private CRUDController crudController;

    public ReadHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        add(new JLabel("ID del Huésped a leer:"));
        add(idField);

        JButton readButton = new JButton("Leer Huésped");
        readButton.addActionListener(e -> leerHuesped());
        add(readButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void leerHuesped() {
        try {
            int idHuesped = Integer.parseInt(idField.getText());
            Huesped huesped = crudController.readHuesped(idHuesped);

            if (huesped != null) {
                JOptionPane.showMessageDialog(this, "Huésped leído: " + huesped.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el Huésped con ID: " + idHuesped, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


package org.example.Ui.CrudHotel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

public class DeleteHotelPanel extends JPanel {
    private JTextField idField;

    CRUDController crudController = new CRUDController();

    public DeleteHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField();
        idField.setPreferredSize(new Dimension(10, 30)); // Establecer un tamaño preferido

        add(new JLabel("Id a Eliminar:"));
        add(idField);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> eliminarHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        add(deleteButton);
        add(backButton);
    }

    private void eliminarHotel() {
        try {
            int id = Integer.parseInt(idField.getText());
            crudController.deleteHotel(id);
            JOptionPane.showMessageDialog(this, "Hotel " + id + " eliminado exitosamente");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
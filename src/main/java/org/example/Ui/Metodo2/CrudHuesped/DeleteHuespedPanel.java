package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;

public class DeleteHuespedPanel extends JPanel {
    private JTextField idField;
    private CRUDController crudController;

    public DeleteHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        add(new JLabel("ID del Huésped a eliminar:"));
        add(idField);

        JButton deleteButton = new JButton("Eliminar Huésped");
        deleteButton.addActionListener(e -> eliminarHuesped());
        add(deleteButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));
        add(backButton);
    }

    private void eliminarHuesped() {
        
        int idHuesped = Integer.parseInt(idField.getText());
        crudController.deleteHuesped(idHuesped);
        
    }
}


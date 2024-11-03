package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;
public class DeleteReservaPanel extends JPanel {
    private JTextField codReservaField;
    private CRUDController crudController;

    public DeleteReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        codReservaField = new JTextField(10);
        add(new JLabel("Código de la Reserva a eliminar:"));
        add(codReservaField);

        JButton deleteButton = new JButton("Eliminar Reserva");
        deleteButton.addActionListener(e -> eliminarReserva());
        add(deleteButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));
        add(backButton);
    }

    private void eliminarReserva() {

        int codReserva = Integer.parseInt(codReservaField.getText());
        crudController.deleteReserva(codReserva);
        
    }
}


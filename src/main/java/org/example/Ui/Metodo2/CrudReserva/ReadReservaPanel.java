package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;

public class ReadReservaPanel extends JPanel {
    private JTextField codReservaField;
    private CRUDController crudController;

    public ReadReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        codReservaField = new JTextField(10);
        add(new JLabel("Código de la Reserva a leer:"));
        add(codReservaField);

        JButton readButton = new JButton("Leer Reserva");
        readButton.addActionListener(e -> leerReserva());
        add(readButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));
        add(backButton);
    }

    private void leerReserva() {
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            Reserva reserva = crudController.readReserva(codReserva);

            if (reserva != null) {
                JOptionPane.showMessageDialog(this, "Reserva leída: " + reserva.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la Reserva con código: " + codReserva, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

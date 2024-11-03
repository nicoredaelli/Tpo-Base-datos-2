package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;


public class CreateReservaPanel extends JPanel {
    private JTextField checkinField, checkoutField, tarifaField, idHotelField, idHabitacionField, idHuespedField;
    private JComboBox<EstadoReserva> estadoReservaComboBox;
    private CRUDController crudController;

    public CreateReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        checkinField = new JTextField(20);
        checkoutField = new JTextField(20);
        tarifaField = new JTextField(10);
        idHotelField = new JTextField(5);
        idHabitacionField = new JTextField(5);
        idHuespedField = new JTextField(5);

        estadoReservaComboBox = new JComboBox<>(EstadoReserva.values());

        add(new JLabel("Fecha de Check-in (yyyy-MM-dd):"));
        add(checkinField);
        add(new JLabel("Fecha de Check-out (yyyy-MM-dd):"));
        add(checkoutField);
        add(new JLabel("Estado de la Reserva:"));
        add(estadoReservaComboBox);
        add(new JLabel("Tarifa:"));
        add(tarifaField);
        add(new JLabel("ID del Hotel:"));
        add(idHotelField);
        add(new JLabel("ID de la Habitación:"));
        add(idHabitacionField);
        add(new JLabel("ID del Huésped:"));
        add(idHuespedField);

        JButton createButton = new JButton("Crear Reserva");
        createButton.addActionListener(e -> crearReserva());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));

        add(createButton);
        add(backButton);
    }

    private void crearReserva() {
        try {
            int codReserva = crudController.getUltimoCodReserva() + 1;
            Reserva nuevaReserva = new Reserva(
                codReserva,
                new SimpleDateFormat("yyyy-MM-dd").parse(checkinField.getText()),
                new SimpleDateFormat("yyyy-MM-dd").parse(checkoutField.getText()),
                (EstadoReserva) estadoReservaComboBox.getSelectedItem(),
                Double.parseDouble(tarifaField.getText()),
                Integer.parseInt(idHotelField.getText()),
                Integer.parseInt(idHabitacionField.getText()),
                Integer.parseInt(idHuespedField.getText())
            );

            crudController.createReserva(nuevaReserva);
            JOptionPane.showMessageDialog(this, "Reserva creada exitosamente.");
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

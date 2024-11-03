package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;

public class UpdateReservaPanel extends JPanel {
    private JTextField codReservaField, checkinField, checkoutField, tarifaField, idHotelField, idHabitacionField, idHuespedField;
    private JComboBox<EstadoReserva> estadoReservaComboBox;
    private JButton loadButton, updateButton;
    private CRUDController crudController;
    private Reserva reserva;

    public UpdateReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        codReservaField = new JTextField(10);
        checkinField = new JTextField(20);
        checkoutField = new JTextField(20);
        tarifaField = new JTextField(10);
        idHotelField = new JTextField(5);
        idHabitacionField = new JTextField(5);
        idHuespedField = new JTextField(5);

        estadoReservaComboBox = new JComboBox<>(EstadoReserva.values());

        add(new JLabel("Código de la Reserva a actualizar:"));
        add(codReservaField);

        loadButton = new JButton("Cargar Reserva");
        loadButton.addActionListener(e -> cargarReserva());
        add(loadButton);

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

        updateButton = new JButton("Actualizar Reserva");
        updateButton.addActionListener(e -> actualizarReserva());
        updateButton.setEnabled(false);
        add(updateButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));
        add(backButton);
    }

    private void cargarReserva() {
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            reserva = crudController.readReserva(codReserva);

            if (reserva != null) {
                checkinField.setText(new SimpleDateFormat("yyyy-MM-dd").format(reserva.getCheckin()));
                checkoutField.setText(new SimpleDateFormat("yyyy-MM-dd").format(reserva.getCheckout()));
                estadoReservaComboBox.setSelectedItem(reserva.getEstadoReserva());
                tarifaField.setText(String.valueOf(reserva.getTarifa()));
                idHotelField.setText(String.valueOf(reserva.getIdHotel()));
                idHabitacionField.setText(String.valueOf(reserva.getIdHabitacion()));
                idHuespedField.setText(String.valueOf(reserva.getIdHuesped()));

                updateButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Reserva cargada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la Reserva con código: " + codReserva, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarReserva() {
        try {
            reserva.setCheckin(new SimpleDateFormat("yyyy-MM-dd").parse(checkinField.getText()));
            reserva.setCheckout(new SimpleDateFormat("yyyy-MM-dd").parse(checkoutField.getText()));
            reserva.setEstadoReserva((EstadoReserva) estadoReservaComboBox.getSelectedItem());
            reserva.setTarifa(Double.parseDouble(tarifaField.getText()));
            reserva.setIdHotel(Integer.parseInt(idHotelField.getText()));
            reserva.setIdHabitacion(Integer.parseInt(idHabitacionField.getText()));
            reserva.setIdHuesped(Integer.parseInt(idHuespedField.getText()));

            crudController.updateReserva(reserva);
            JOptionPane.showMessageDialog(this, "Reserva actualizada exitosamente.");
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

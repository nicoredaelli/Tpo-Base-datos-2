package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UpdateReservaPanel extends JPanel {
    private JTextField codReservaField, checkinField, checkoutField, tarifaField;
    private JComboBox<Integer> idHotelComboBox, idHabitacionComboBox, idHuespedComboBox;
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

        // Inicializar JComboBox para IDs y estado
        idHotelComboBox = new JComboBox<>(getHotelIds());
        idHabitacionComboBox = new JComboBox<>(getHabitacionIds());
        idHuespedComboBox = new JComboBox<>(getHuespedIds());
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
        add(idHotelComboBox);
        add(new JLabel("ID de la Habitación:"));
        add(idHabitacionComboBox);
        add(new JLabel("ID del Huésped:"));
        add(idHuespedComboBox);

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
                idHotelComboBox.setSelectedItem(reserva.getIdHotel());
                idHabitacionComboBox.setSelectedItem(reserva.getIdHabitacion());
                idHuespedComboBox.setSelectedItem(reserva.getIdHuesped());

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
            reserva.setIdHotel((Integer) idHotelComboBox.getSelectedItem());
            reserva.setIdHabitacion((Integer) idHabitacionComboBox.getSelectedItem());
            reserva.setIdHuesped((Integer) idHuespedComboBox.getSelectedItem());

            crudController.updateReserva(reserva);
            JOptionPane.showMessageDialog(this, "Reserva actualizada exitosamente.");
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos para obtener los IDs desde el CRUDController
    private Integer[] getHotelIds() {
        List<Hotel> hoteles = crudController.getHotelesDisponibles();
        return hoteles.stream().map(Hotel::getIdHotel).toArray(Integer[]::new);
    }

    private Integer[] getHabitacionIds() {
        List<Habitacion> habitaciones = crudController.getHabitacionesDisponibles();
        return habitaciones.stream().map(Habitacion::getNroHabitacion).toArray(Integer[]::new);
    }

    private Integer[] getHuespedIds() {
        List<Huesped> huespedes = crudController.getHuespedesDisponibles();
        return huespedes.stream().map(Huesped::getIdHuesped).toArray(Integer[]::new);
    }
}

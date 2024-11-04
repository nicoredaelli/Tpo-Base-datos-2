package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;
import java.util.List;


public class CreateReservaPanel extends JPanel {
    private JTextField checkinField, checkoutField, tarifaField;
    private JComboBox<Integer> idHotelComboBox, idHabitacionComboBox, idHuespedComboBox;
    private CRUDController crudController;

    public CreateReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        checkinField = new JTextField(20);
        checkoutField = new JTextField(20);
        tarifaField = new JTextField(10);

        // Inicializar JComboBox para IDs
        idHotelComboBox = new JComboBox<>(getHotelIds());
        idHabitacionComboBox = new JComboBox<>(getHabitacionIds());
        idHuespedComboBox = new JComboBox<>(getHuespedIds());

        add(new JLabel("Fecha de Check-in (yyyy-MM-dd):"));
        add(checkinField);
        add(new JLabel("Fecha de Check-out (yyyy-MM-dd):"));
        add(checkoutField);
        add(new JLabel("Estado de la Reserva:"));
        add(new JLabel("CONFIRMADO")); // Mostrar estado fijo
        add(new JLabel("Tarifa:"));
        add(tarifaField);
        add(new JLabel("ID del Hotel:"));
        add(idHotelComboBox);
        add(new JLabel("ID de la Habitación:"));
        add(idHabitacionComboBox);
        add(new JLabel("ID del Huésped:"));
        add(idHuespedComboBox);

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
                EstadoReserva.CONFIRMADO, // Estado fijo
                Double.parseDouble(tarifaField.getText()),
                (Integer) idHotelComboBox.getSelectedItem(), // Obtener ID seleccionado del JComboBox
                (Integer) idHabitacionComboBox.getSelectedItem(), // Obtener ID seleccionado del JComboBox
                (Integer) idHuespedComboBox.getSelectedItem() // Obtener ID seleccionado del JComboBox
            );

            crudController.createReserva(nuevaReserva);
            JOptionPane.showMessageDialog(this, "Reserva creada exitosamente.");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error en las fechas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en la tarifa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al crear la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;
import java.util.List;

public class CreateReservaPanel extends JPanel {
    private JTextField checkinField, checkoutField, tarifaField;
    private JComboBox<String> hotelComboBox, habitacionComboBox, huespedComboBox;
    private CRUDController crudController;

    public CreateReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        checkinField = new JTextField(20);
        checkoutField = new JTextField(20);
        tarifaField = new JTextField(10);

        // Inicializar JComboBox con detalles
        hotelComboBox = new JComboBox<>(getHotelDetails());
        habitacionComboBox = new JComboBox<>();
        habitacionComboBox.setEnabled(false); // Deshabilitado hasta seleccionar hotel
        huespedComboBox = new JComboBox<>(getHuespedDetails());

        hotelComboBox.addActionListener(e -> actualizarHabitaciones());

        inputPanel.add(new JLabel("Fecha de Check-in (yyyy-MM-dd):"));
        inputPanel.add(checkinField);
        inputPanel.add(new JLabel("Fecha de Check-out (yyyy-MM-dd):"));
        inputPanel.add(checkoutField);
        inputPanel.add(new JLabel("Estado de la Reserva:"));
        inputPanel.add(new JLabel("CONFIRMADO")); // Mostrar estado fijo
        inputPanel.add(new JLabel("Tarifa:"));
        inputPanel.add(tarifaField);
        inputPanel.add(new JLabel("Hotel:"));
        inputPanel.add(hotelComboBox);
        inputPanel.add(new JLabel("Habitación:"));
        inputPanel.add(habitacionComboBox);
        inputPanel.add(new JLabel("Huésped:"));
        inputPanel.add(huespedComboBox);

        JButton createButton = new JButton("Crear Reserva");
        createButton.addActionListener(e -> crearReserva());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));

        inputPanel.add(createButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void actualizarHabitaciones() {
        // Limpiar el JComboBox de habitaciones
        habitacionComboBox.removeAllItems();

        // Obtener el hotel seleccionado
        String selectedHotel = (String) hotelComboBox.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            // Obtener las habitaciones correspondientes al hotel seleccionado
            List<Habitacion> habitaciones = crudController.getRoomsByHotelId(hotelId);
            for (Habitacion habitacion : habitaciones) {
                habitacionComboBox.addItem(habitacion.getIdHabitacion() + " - Número: " + habitacion.getNroHabitacion() + ", Tipo: " + habitacion.getTipoHabitacion());
            }

            habitacionComboBox.setEnabled(true); // Habilitar el JComboBox de habitaciones
        }
    }

    private void crearReserva() {
        try {
            int codReserva = crudController.getUltimoCodReserva() + 1;
            int hotelId = Integer.parseInt(((String) hotelComboBox.getSelectedItem()).split(" - ")[0]);
            int habitacionId = Integer.parseInt(((String) habitacionComboBox.getSelectedItem()).split(" - ")[0]);
            int huespedId = Integer.parseInt(((String) huespedComboBox.getSelectedItem()).split(" - ")[0]);

            Reserva nuevaReserva = new Reserva(
                    codReserva,
                    new SimpleDateFormat("yyyy-MM-dd").parse(checkinField.getText()),
                    new SimpleDateFormat("yyyy-MM-dd").parse(checkoutField.getText()),
                    EstadoReserva.CONFIRMADO, // Estado fijo
                    Double.parseDouble(tarifaField.getText()),
                    hotelId,
                    habitacionId,
                    huespedId
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

    // Métodos para obtener los detalles desde el CRUDController
    private String[] getHotelDetails() {
        List<Hotel> hoteles = crudController.getAllHoteles();
        return hoteles.stream()
                .map(hotel -> hotel.getIdHotel() + " - " + hotel.getNombre())
                .toArray(String[]::new);
    }

    private String[] getHuespedDetails() {
        List<Huesped> huespedes = crudController.getAllHuespedes();
        return huespedes.stream()
                .map(huesped -> huesped.getIdHuesped() + " - " + huesped.getNombre() + " " + huesped.getApellido())
                .toArray(String[]::new);
    }
}

package org.example.Ui.Metodo10;

import org.bson.Document;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;
import org.example.entidades.Huesped;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservasPorFechaPanel extends JPanel {
    private JTextField fechaField;
    private JComboBox<String> hotelDropdown;
    private JTextArea resultArea;
    private CRUDController crudController;
    private DatabaseQueryController databaseQueryController;

    public ReservasPorFechaPanel(MainFrame mainFrame) {
        this.crudController = new CRUDController();
        this.databaseQueryController = new DatabaseQueryController();
        setLayout(new BorderLayout());

        // Panel de entrada para la fecha y selección del hotel
        JPanel inputPanel = new JPanel(new FlowLayout());

        inputPanel.add(new JLabel("Ingrese la Fecha (YYYY-MM-DD):"));
        fechaField = new JTextField(10);
        inputPanel.add(fechaField);

        inputPanel.add(new JLabel("Seleccione el Hotel:"));
        hotelDropdown = new JComboBox<>();
        loadHotelsIntoDropdown(); // Metodo para cargar los hoteles en el JComboBox
        inputPanel.add(hotelDropdown);

        // Botón para buscar reservas
        JButton searchButton = new JButton("Buscar Reservas");
        searchButton.addActionListener(e -> buscarReservasPorFecha());
        inputPanel.add(searchButton);

        // Botón para regresar al menú principal
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("Menu"));
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        // Área de texto para mostrar los resultados
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    // Metodo para cargar la lista de hoteles en el JComboBox
    private void loadHotelsIntoDropdown() {
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        if (hotelesDisponibles != null && !hotelesDisponibles.isEmpty()) {
            for (Hotel hotel : hotelesDisponibles) {
                hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
            }
        } else {
            hotelDropdown.addItem("No hay hoteles disponibles");
        }
    }

    private void buscarReservasPorFecha() {
        resultArea.setText(""); // Limpiar el área de resultados
        try {
            // Parsear la fecha ingresada por el usuario
            Date fechaDada = parseDate(fechaField.getText());

            // Obtener el hotel seleccionado
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            if (selectedHotel == null || selectedHotel.equals("No hay hoteles disponibles")) {
                JOptionPane.showMessageDialog(this, "Seleccione un hotel válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            // Llama al CRUDController para obtener reservas en la fecha y hotel indicados
            List<Document> reservas = databaseQueryController.ReservasPorFechaYHotel(fechaDada, hotelId);

            if (reservas != null && !reservas.isEmpty()) {
                StringBuilder result = new StringBuilder("Reservas confirmadas en la fecha y hotel indicados:\n");
                for (Document reservaDoc : reservas) {
                    result.append("Código de Reserva: ").append(reservaDoc.getInteger("cod_reserva")).append("\n");
                    result.append("Check-in: ").append(reservaDoc.getDate("checkin")).append("\n");
                    result.append("Check-out: ").append(reservaDoc.getDate("checkout")).append("\n");
                    result.append("Estado: ").append(reservaDoc.getString("estado_reserva")).append("\n");
                    result.append("Tarifa: ").append(reservaDoc.getDouble("tarifa")).append("\n");

                    int idHabitacion = reservaDoc.getInteger("id_habitacion");
                    int idHuesped = reservaDoc.getInteger("id_huesped");

                    // Obtener y mostrar información de la habitación
                    Habitacion habitacion = crudController.readHabitacion(idHabitacion);
                    if (habitacion != null) {
                        result.append("\nInformación de la Habitación:\n");
                        result.append("  Número de Habitación: ").append(habitacion.getNroHabitacion()).append("\n");
                        result.append("  Tipo de Habitación: ").append(habitacion.getTipoHabitacion()).append("\n");
                    } else {
                        result.append("\nHabitación no encontrada para ID ").append(idHabitacion).append("\n");
                    }

                    // Obtener y mostrar información del huésped
                    Huesped huesped = crudController.readHuesped(idHuesped);
                    if (huesped != null) {
                        result.append("\nInformación del Huésped:\n");
                        result.append("  Nombre: ").append(huesped.getNombre()).append(" ").append(huesped.getApellido()).append("\n");
                        result.append("  Teléfono: ").append(huesped.getTelefono()).append("\n");
                        result.append("  Email: ").append(huesped.getEmail()).append("\n");
                    } else {
                        result.append("\nHuésped no encontrado para ID ").append(idHuesped).append("\n");
                    }

                    result.append("\n---------------------------------\n");
                }
                resultArea.setText(result.toString());
            } else {
                resultArea.setText("No se encontraron reservas confirmadas en la fecha y hotel indicados.");
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Activa validación estricta
        return dateFormat.parse(dateString); // Lanza ParseException si el formato es incorrecto
    }
}



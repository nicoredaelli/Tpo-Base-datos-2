package org.example.Ui.Metodo1.CrudHabitaciones;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;
import org.example.entidades.Habitacion;

public class DeleteRoomPanel extends JPanel {
    private JComboBox<String> roomDropdown;
    private JTextArea roomDetailsArea;
    private MainFrame mainFrame;
    private CRUDController crudController;
    private int hotelId;

    public DeleteRoomPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomDropdown = new JComboBox<>();

        JButton deleteButton = new JButton("Eliminar Habitación");
        deleteButton.addActionListener(e -> deleteRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("DeleteRoomHotelSelectionPanel"));

        JButton showDetailsButton = new JButton("Mostrar Detalles");
        showDetailsButton.addActionListener(e -> showRoomDetails());

        roomDetailsArea = new JTextArea(5, 30);
        roomDetailsArea.setEditable(false); // El área de detalles solo mostrará información

        add(new JLabel("Seleccione una habitación:"));
        add(roomDropdown);
        add(showDetailsButton);
        add(new JScrollPane(roomDetailsArea)); // Agregar el JTextArea en un JScrollPane
        add(deleteButton);
        add(backButton);
    }

    // Método para cargar las habitaciones del hotel seleccionado
    public void loadRooms(int hotelId) {
        this.hotelId = hotelId; // Almacena el hotelId
        roomDropdown.removeAllItems(); // Limpiar el JComboBox

        List<Habitacion> habitaciones = crudController.getRoomsByHotelId(hotelId);
        for (Habitacion habitacion : habitaciones) {
            roomDropdown.addItem(habitacion.getIdHabitacion() + " - " + habitacion.getNroHabitacion());
        }
        roomDetailsArea.setText(""); // Limpiar el área de detalles cuando se recargan las habitaciones
    }

    private void deleteRoom() {
        String selectedRoom = (String) roomDropdown.getSelectedItem();
        if (selectedRoom != null) {
            int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);
            crudController.deleteHabitacion(roomId); // Elimina la habitación seleccionada
            JOptionPane.showMessageDialog(this, "Habitación eliminada.");

            // Recarga las habitaciones para reflejar los cambios
            loadRooms(hotelId);
        }
    }

    private void showRoomDetails() {
        String selectedRoom = (String) roomDropdown.getSelectedItem();
        if (selectedRoom != null) {
            int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);
            Habitacion habitacion = crudController.readHabitacion(roomId); // Obtiene la habitación seleccionada

            if (habitacion != null) {
                StringBuilder details = new StringBuilder();
                details.append("ID Habitación: ").append(habitacion.getIdHabitacion()).append("\n");
                details.append("Número de Habitación: ").append(habitacion.getNroHabitacion()).append("\n");
                details.append("Tipo de Habitación: ").append(habitacion.getTipoHabitacion()).append("\n");
                details.append("ID Hotel: ").append(habitacion.getIdHotel()).append("\n");
                details.append("Amenities:\n");

                // Itera sobre cada amenity y obtiene sus detalles
                for (Integer amenityId : habitacion.getAmenities()) {
                    Amenity amenity = crudController.readAmenity(amenityId);
                    if (amenity != null) {
                        details.append("  - ").append(amenity.getNombre()).append(": ").append(amenity.getDescripcion()).append("\n");
                    }
                }

                // Muestra los detalles en el JTextArea
                roomDetailsArea.setText(details.toString());
            } else {
                roomDetailsArea.setText("No se encontró información de la habitación.");
            }
        }
    }

}

package org.example.Ui.Metodo1.CrudHotel;

import java.awt.Dimension;
import java.util.List;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;

public class DeleteHotelPanel extends JPanel {
    private JComboBox<String> hotelDropdown; // Cuadro desplegable para seleccionar el hotel
    private CRUDController crudController = new CRUDController();

    public DeleteHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de hoteles disponibles desde el CRUDController
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        String[] hotelNames = hotelesDisponibles.stream().map(Hotel::getNombre).toArray(String[]::new);

        // Crear el JComboBox para seleccionar el hotel
        hotelDropdown = new JComboBox<>(hotelNames);
        hotelDropdown.setPreferredSize(new Dimension(5, -5));
        add(new JLabel("Seleccione un hotel a eliminar:"));
        add(hotelDropdown);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> eliminarHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        add(deleteButton);
        add(backButton);
    }

    private void eliminarHotel() {
        try {
            String selectedHotelName = (String) hotelDropdown.getSelectedItem();
            Hotel hotel = crudController.getAllHoteles().stream()
                .filter(h -> h.getNombre().equals(selectedHotelName))
                .findFirst()
                .orElse(null);

            if (hotel != null) {
                int idHotel = hotel.getIdHotel(); // Obtener el ID del hotel seleccionado
                crudController.deleteHotel(idHotel);
                JOptionPane.showMessageDialog(this, "Hotel " + selectedHotelName + " eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Hotel no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

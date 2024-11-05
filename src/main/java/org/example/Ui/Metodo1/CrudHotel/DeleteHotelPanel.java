package org.example.Ui.Metodo1.CrudHotel;

import java.awt.*;
import java.util.List;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;

public class DeleteHotelPanel extends JPanel {
    private JComboBox<String> hotelDropdown; // Cuadro desplegable para seleccionar el hotel
    private CRUDController crudController = new CRUDController();

    public DeleteHotelPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir márgenes

        JLabel titleLabel = new JLabel("Seleccione un hotel a eliminar:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear el JComboBox para seleccionar el hotel
        hotelDropdown = new JComboBox<>();
        hotelDropdown.setMaximumSize(new Dimension(250, 30)); // Ajustar el tamaño del combo box
        loadHotels();

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(e -> eliminarHotel());

        JButton backButton = new JButton("Regresar");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // Espacio entre componentes
        contentPanel.add(hotelDropdown);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(deleteButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(backButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadHotels() {
        // Cargar la lista de hoteles en el JComboBox
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }
    }

    private void eliminarHotel() {
        try {
            String selectedHotelName = (String) hotelDropdown.getSelectedItem();
            Hotel hotel = crudController.getAllHoteles().stream()
                    .filter(h -> (h.getIdHotel() + " - " + h.getNombre()).equals(selectedHotelName))
                    .findFirst()
                    .orElse(null);

            if (hotel != null) {
                int idHotel = hotel.getIdHotel(); // Obtener el ID del hotel seleccionado
                crudController.deleteHotel(idHotel);
                JOptionPane.showMessageDialog(this, "Hotel " + hotel.getNombre() + " eliminado exitosamente.");
                loadHotels(); // Recargar la lista de hoteles después de eliminar uno
            } else {
                JOptionPane.showMessageDialog(this, "Hotel no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al eliminar el hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

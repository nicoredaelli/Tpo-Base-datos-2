package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteRoomHotelSelectionPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private MainFrame mainFrame;
    private CRUDController crudController;

    public DeleteRoomHotelSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400)); // Tamaño aumentado del panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espacio entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Etiqueta de selección de hotel
        JLabel selectHotelLabel = new JLabel("Seleccione un hotel:");
        selectHotelLabel.setHorizontalAlignment(JLabel.CENTER);
        selectHotelLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente aumentada y en negrita
        add(selectHotelLabel, gbc);

        // Dropdown de hoteles
        gbc.gridy++;
        hotelDropdown = new JComboBox<>();
        hotelDropdown.setPreferredSize(new Dimension(200, 30)); // Tamaño aumentado del combobox
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }
        add(hotelDropdown, gbc);

        // Botón de seleccionar hotel
        gbc.gridy++;
        JButton selectHotelButton = new JButton("Seleccionar Hotel");
        selectHotelButton.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        selectHotelButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente aumentada
        selectHotelButton.addActionListener(e -> selectHotel());
        add(selectHotelButton, gbc);

        // Botón de regresar
        gbc.gridy++;
        JButton backButton = new JButton("Regresar");
        backButton.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        backButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente aumentada
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));
        add(backButton, gbc);
    }

    private void selectHotel() {
        String selectedHotel = (String) hotelDropdown.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);
            mainFrame.showDeleteRoomPanel(hotelId); // Muestra el panel de eliminación de habitaciones para el hotel seleccionado
        }
    }
}

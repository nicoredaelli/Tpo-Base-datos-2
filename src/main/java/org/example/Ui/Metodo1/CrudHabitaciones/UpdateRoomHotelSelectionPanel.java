package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateRoomHotelSelectionPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private MainFrame mainFrame;
    private CRUDController crudController;

    public UpdateRoomHotelSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400)); // Aumentar el tamaño del panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Más espacio entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Etiqueta de selección de hotel
        JLabel selectHotelLabel = new JLabel("Seleccione un hotel:");
        selectHotelLabel.setHorizontalAlignment(JLabel.CENTER);
        selectHotelLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Tamaño y estilo de fuente aumentados
        add(selectHotelLabel, gbc);

        // Dropdown de hoteles
        gbc.gridy++;
        hotelDropdown = new JComboBox<>();
        hotelDropdown.setPreferredSize(new Dimension(200, 30)); // Aumentar el tamaño del combobox
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }
        add(hotelDropdown, gbc);

        // Botón de seleccionar hotel
        gbc.gridy++;
        JButton selectHotelButton = new JButton("Seleccionar Hotel");
        selectHotelButton.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        selectHotelButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Tamaño de fuente aumentado
        selectHotelButton.addActionListener(e -> selectHotel());
        add(selectHotelButton, gbc);

        // Botón de regresar
        gbc.gridy++;
        JButton backButton = new JButton("Regresar");
        backButton.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        backButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Tamaño de fuente aumentado
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));
        add(backButton, gbc);
    }

    private void selectHotel() {
        String selectedHotel = (String) hotelDropdown.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);
            mainFrame.showUpdateRoomPanel(hotelId); // Muestra el panel de modificación de habitaciones para el hotel seleccionado
        }
    }
}

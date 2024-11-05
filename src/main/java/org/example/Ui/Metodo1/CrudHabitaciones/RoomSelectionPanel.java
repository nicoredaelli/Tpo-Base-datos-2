package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class RoomSelectionPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;
    private MainFrame mainFrame;

    public RoomSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Añadir márgenes

        JLabel titleLabel = new JLabel("Seleccione un hotel:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hotelDropdown = new JComboBox<>();
        hotelDropdown.setMaximumSize(new Dimension(250, 30)); // Ajustar el tamaño del combo box
        loadHotels();

        JButton selectHotelButton = new JButton("Seleccionar Hotel");
        selectHotelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectHotelButton.addActionListener(this::selectHotel);

        JButton backButton = new JButton("Regresar");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10)); // Espacio entre componentes
        contentPanel.add(hotelDropdown);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(selectHotelButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(backButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadHotels() {
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }
    }

    private void selectHotel(ActionEvent e) {
        String selectedHotel = (String) hotelDropdown.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);
            mainFrame.showRoomPanel(hotelId);
            mainFrame.showPanel("ReadRoomPanel");
        }
    }
}

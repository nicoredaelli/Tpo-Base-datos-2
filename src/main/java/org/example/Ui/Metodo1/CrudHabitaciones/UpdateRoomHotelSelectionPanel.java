package org.example.Ui.Metodo1.CrudHabitaciones;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;
import java.util.List;

public class UpdateRoomHotelSelectionPanel extends JPanel {
    private JComboBox<String> hotelDropdown;
    private MainFrame mainFrame;
    private CRUDController crudController;

    public UpdateRoomHotelSelectionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        hotelDropdown = new JComboBox<>();

        // Cargar la lista de hoteles disponibles
        List<Hotel> hotelesDisponibles = crudController.getAllHoteles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }

        JButton selectHotelButton = new JButton("Seleccionar Hotel");
        selectHotelButton.addActionListener(e -> selectHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(new JLabel("Seleccione un hotel:"));
        add(hotelDropdown);
        add(selectHotelButton);
        add(backButton);
    }

    private void selectHotel() {
        String selectedHotel = (String) hotelDropdown.getSelectedItem();
        if (selectedHotel != null) {
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);
            mainFrame.showUpdateRoomPanel(hotelId); // Muestra el panel de modificación de habitaciones para el hotel seleccionado
        }
    }
}

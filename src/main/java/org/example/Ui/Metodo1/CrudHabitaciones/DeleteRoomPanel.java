package org.example.Ui.Metodo1.CrudHabitaciones;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;
//lo mismo agregarle el id del hotel 
public class DeleteRoomPanel extends JPanel {
    private JTextField roomNumberField;
    private JComboBox<String> hotelDropdown;
    private CRUDController crudController;

    public DeleteRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomNumberField = new JTextField(10);
        hotelDropdown = new JComboBox<>();

        // Cargar hoteles en el JComboBox
        List<Hotel> hotelesDisponibles = crudController.getHotelesDisponibles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre()); // Muestra ID y nombre
        }

        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("Hotel:"));
        add(hotelDropdown);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> deleteRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(deleteButton);
        add(backButton);
    }

    private void deleteRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]); // Extrae el ID del hotel

            crudController.deleteHabitacion(roomNumber);

            JOptionPane.showMessageDialog(this, "Habitación eliminada exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de habitación válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

 

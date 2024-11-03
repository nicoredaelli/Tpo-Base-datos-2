package org.example.Ui.Metodo1.CrudHabitaciones;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;


public class ReadRoomPanel extends JPanel {
    private JTextField roomNumberField;
    private JComboBox<String> hotelDropdown;
    private JTextArea roomDetailsArea;
    private CRUDController crudController;

    public ReadRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomNumberField = new JTextField(10);
        hotelDropdown = new JComboBox<>();
        
        // Cargar hoteles en el JComboBox
        List<Hotel> hotelesDisponibles = crudController.getHotelesDisponibles();
        for (Hotel hotel : hotelesDisponibles) {
            hotelDropdown.addItem(hotel.getIdHotel() + " - " + hotel.getNombre());
        }

        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("Hotel:"));
        add(hotelDropdown);

        JButton readButton = new JButton("Leer");
        readButton.addActionListener(e -> readRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel"));

        add(readButton);

        roomDetailsArea = new JTextArea(10, 30);
        roomDetailsArea.setEditable(false);
        add(new JScrollPane(roomDetailsArea));

       
        add(backButton);
    }

    private void readRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String selectedHotel = (String) hotelDropdown.getSelectedItem();
            int hotelId = Integer.parseInt(selectedHotel.split(" - ")[0]);

            Habitacion habitacion = crudController.readHabitacion(roomNumber);

            if (habitacion != null) {
                roomDetailsArea.setText(habitacion.toString());
            } else {
                roomDetailsArea.setText("Habitación no encontrada.");
            }
        } catch (NumberFormatException e) {
            roomDetailsArea.setText("Ingrese un número de habitación válido.");
        }
    }
}

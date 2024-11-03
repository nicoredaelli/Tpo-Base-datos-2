package org.example.Ui.CrudHabitaciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;


//agregarle a la entidad read que para buscar la habitacion le pida el id del hotel, porque sino es un quilombo
public class ReedRoomPanel extends JPanel {
   private JTextField roomNumberField, hotelIdField;
    private CRUDController crudController;

    public ReedRoomPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        roomNumberField = new JTextField(10);
        hotelIdField = new JTextField(10);
        

        add(new JLabel("Número de Habitación:"));
        add(roomNumberField);
        add(new JLabel("ID del Hotel:"));
        add(hotelIdField);
        

        JButton readButton = new JButton("Leer");
        readButton.addActionListener(e -> LeerRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel")); // Ajustar nombre según el panel anterior

        add(readButton);
        add(backButton);
    }

    private void LeerRoom() {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            int hotelId = Integer.parseInt(hotelIdField.getText());
            
            
            crudController.readHabitacion(roomNumber);

          
    }
}

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;
import org.example.entidades.Hotel;


public class ReedRoomPanel extends JPanel {
    private JTextField roomNumberField, hotelIdField;
    private JTextArea roomDetailsArea; // Área para mostrar los detalles de la habitación
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
        readButton.addActionListener(e -> leerRoom());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel")); // Ajustar nombre según el panel anterior

        add(readButton);
        

        // Configuración del área de texto para mostrar los detalles de la habitación
        roomDetailsArea = new JTextArea(10, 30);
        roomDetailsArea.setEditable(false); // Hacer que el área de texto no sea editable
        add(new JScrollPane(roomDetailsArea)); // Añadir un scroll para el área de texto
        add(backButton);
    }

    private void leerRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            int hotelId = Integer.parseInt(hotelIdField.getText());
            
            Habitacion habitacion = crudController.readHabitacion(roomNumber); // Método que obtiene la habitación por número y hotel
            
            if (habitacion != null) {
                // Mostrar los detalles de la habitación en el área de texto
                roomDetailsArea.setText(habitacion.toString()); // Asegúrate de que Habitacion tenga un método toString() que muestre los detalles
            } else {
                roomDetailsArea.setText("Habitación no encontrada.");
            }
        } catch (NumberFormatException e) {
            roomDetailsArea.setText("Por favor, ingrese valores válidos en los campos numéricos.");
        }
    }
}
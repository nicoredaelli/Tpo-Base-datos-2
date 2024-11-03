package org.example.Ui.CrudHotel;

import java.awt.Dimension;
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
import org.example.entidades.Hotel;

public class ReadHotelPanel extends JPanel {
    
    private JTextField idField;
    private JTextArea hotelDetailsArea; // Área para mostrar los detalles del hotel

    CRUDController crudController = new CRUDController();

    public ReadHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField();
        idField.setPreferredSize(new Dimension(10, 30)); // Establecer un tamaño preferido

        add(new JLabel("Id del hotel:"));
        add(idField);

        JButton readButton = new JButton("Leer");
        readButton.addActionListener(e -> leerHotel());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        add(readButton);
        

        // Configuración del área de texto para mostrar los detalles del hotel
        hotelDetailsArea = new JTextArea(10, 30);
        hotelDetailsArea.setEditable(false); // Hacer que el área de texto no sea editable
        add(new JScrollPane(hotelDetailsArea)); // Añadir un scroll para el área de texto

        add(backButton);
    }

    private void leerHotel() {
        try {
            int id = Integer.parseInt(idField.getText());
            Hotel hotel = crudController.readHotel(id); // Método que obtiene el hotel por ID

            if (hotel != null) {
                // Mostrar los detalles del hotel en el área de texto
                hotelDetailsArea.setText(hotel.toString()); // Asegúrate de que Hotel tenga un método toString() que muestre los detalles
            } else {
                hotelDetailsArea.setText("Hotel no encontrado.");
            }
        } catch (NumberFormatException e) {
            hotelDetailsArea.setText("Por favor, ingrese un ID válido.");
        }
    }       
}
package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.*;


public class ReadReservaPanel extends JPanel {
    private JTextField codReservaField;
    private JTextArea reservaInfoArea;
    private CRUDController crudController;

    public ReadReservaPanel(MainFrame mainFrame) {
        crudController = new CRUDController();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        codReservaField = new JTextField(10);
        add(new JLabel("Código de la Reserva a leer:"));
        add(codReservaField);

        JButton readButton = new JButton("Leer Reserva");
        readButton.addActionListener(e -> leerReserva());
        add(readButton);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("ReservaCRUDPanel"));
        add(backButton);

        // Área de texto para mostrar la información de la reserva
        reservaInfoArea = new JTextArea(8, 30);
        reservaInfoArea.setEditable(false); // Solo lectura
        reservaInfoArea.setLineWrap(true);
        reservaInfoArea.setWrapStyleWord(true);
        
        // Panel con borde blanco para la información de la reserva
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        infoPanel.add(new JScrollPane(reservaInfoArea), BorderLayout.CENTER);
        
        add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
        add(infoPanel);
    }

    private void leerReserva() {
        try {
            int codReserva = Integer.parseInt(codReservaField.getText());
            Reserva reserva = crudController.readReserva(codReserva);
            
            if (reserva != null) {
               // Obtener los nombres del hotel y del huésped mediante sus IDs
               String nombreHotel = crudController.readHotel(reserva.getIdHotel()).getNombre();
               String nombreHuesped = crudController.readHuesped(reserva.getIdHuesped()).getNombre();
               String apellidoHuesped = crudController.readHuesped(reserva.getIdHuesped()).getApellido();
               // Mostrar los detalles de la reserva en el bloque blanco
               reservaInfoArea.setText("Código de Reserva: " + reserva.getCodReserva() + "\n" +
                                       "Check-in: " + reserva.getCheckin() + "\n" +
                                       "Check-out: " + reserva.getCheckout() + "\n" +
                                       "Estado: " + reserva.getEstadoReserva() + "\n" +
                                       "Tarifa: " + reserva.getTarifa() + "\n" +
                                       "Hotel: " + nombreHotel + "\n" +
                                       "Habitación (ID): " + reserva.getIdHabitacion() + "\n" +
                                       "Huésped: " + nombreHuesped +" "+ apellidoHuesped);
           } else {
                reservaInfoArea.setText("No se encontró la Reserva con código: " + codReserva);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de reserva válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

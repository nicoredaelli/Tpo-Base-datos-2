package org.example.Ui.CrudHabitaciones;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
//lo mismo agregarle el id del hotel 
public class DeleteRoomPanel extends JPanel {
    private JTextField roomNumberField, hotelIdField;
     private CRUDController crudController;
 
     public DeleteRoomPanel(MainFrame mainFrame) {
         crudController = new CRUDController();
 
         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
 
         roomNumberField = new JTextField(10);
         hotelIdField = new JTextField(10);
         
 
         add(new JLabel("Número de Habitación:"));
         add(roomNumberField);
         add(new JLabel("ID del Hotel:"));
         add(hotelIdField);
         
 
         JButton DeleteButton = new JButton("Eliminar");
         DeleteButton.addActionListener(e -> DeleteRoom());
 
         JButton backButton = new JButton("Regresar");
         backButton.addActionListener(e -> mainFrame.showPanel("RoomCRUDPanel")); // Ajustar nombre según el panel anterior
 
         add(DeleteButton);
         add(backButton);
     }
 
     private void DeleteRoom() {
             int roomNumber = Integer.parseInt(roomNumberField.getText());
             int hotelId = Integer.parseInt(hotelIdField.getText());
             
             
             crudController.deleteHabitacion(roomNumber);
 
           
     }
 }
 

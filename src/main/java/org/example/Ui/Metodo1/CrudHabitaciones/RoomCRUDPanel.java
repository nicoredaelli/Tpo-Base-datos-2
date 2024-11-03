package org.example.Ui.Metodo1.CrudHabitaciones;
import javax.swing.*;

import org.example.Ui.MainFrame;

public class RoomCRUDPanel extends JPanel {
    public RoomCRUDPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreateRoomPanel")); 

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdateRoomPanel")); 

        
        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReedRoomPanel")); // Cambiar al panel de eliminación


        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeleteRoomPanel")); // Cambiar al panel de eliminación



        
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHHAP")); // Cambiar a "GestiónHHAP" si es necesario

        add(btnCreate);
        add(btnUpdate);
        add(btnRead);
        add(btnDelete);
        add(backButton);
    }
}

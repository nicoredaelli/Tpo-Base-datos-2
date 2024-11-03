package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;

import org.example.Ui.MainFrame;

public class POICRUDPanel extends JPanel {
    public POICRUDPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreatePOIPanel")); 

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdatePOIPanel")); 

        
        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReadPOIPanel")); // Cambiar al panel de eliminación


        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeletePOIPanel")); // Cambiar al panel de eliminación



        
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHHAP")); // Cambiar a "GestiónHHAP" si es necesario

        add(btnCreate);
        add(btnUpdate);
        add(btnRead);
        add(btnDelete);
        add(backButton);
    }
}

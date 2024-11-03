package org.example.Ui.Metodo1.CrudAmenitie;

import javax.swing.*;

import org.example.Ui.MainFrame;

public class AmenityCRUDPanel extends JPanel {
    public AmenityCRUDPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreateAmenitiePanel")); 

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdateAmenitiePanel")); 

        
        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReadAmenitiePanel")); // Cambiar al panel de eliminación


        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeleteAmenitiePanel")); // Cambiar al panel de eliminación



        
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHHAP")); // Cambiar a "GestiónHHAP" si es necesario

        add(btnCreate);
        add(btnUpdate);
        add(btnRead);
        add(btnDelete);
        add(backButton);
    }
}

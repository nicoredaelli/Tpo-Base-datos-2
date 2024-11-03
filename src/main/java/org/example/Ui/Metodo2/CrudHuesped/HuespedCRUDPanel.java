package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.Ui.MainFrame;

public class HuespedCRUDPanel extends JPanel{
    public HuespedCRUDPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreateHuespedPanel")); 

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdateHuespedPanel")); 

        
        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReadHuespedPanel")); // Cambiar al panel de eliminación


        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeleteHuespedPanel")); // Cambiar al panel de eliminación



        
        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHR")); // Cambiar a "GestiónHHAP" si es necesario

        add(btnCreate);
        add(btnUpdate);
        add(btnRead);
        add(btnDelete);
        add(backButton);
    }
}

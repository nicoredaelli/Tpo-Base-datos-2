package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;

public class POICRUDPanel extends JPanel {
    public POICRUDPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crear un panel central con GridLayout para organizar los botones en una columna
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreatePOIPanel"));

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdatePOIPanel"));

        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReadPOIPanel"));

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeletePOIPanel"));

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHHAP"));

        // Añadir los botones al panel de botones
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnRead);
        buttonPanel.add(btnDelete);
        buttonPanel.add(backButton);

        // Añadir el panel de botones al centro del panel principal
        add(buttonPanel, BorderLayout.CENTER);
    }
}

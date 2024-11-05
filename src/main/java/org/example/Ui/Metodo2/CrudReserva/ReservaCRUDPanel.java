package org.example.Ui.Metodo2.CrudReserva;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;

public class ReservaCRUDPanel extends JPanel {
    public ReservaCRUDPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crear un panel central con GridLayout para organizar los botones en una columna
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCreate = new JButton("Crear");
        btnCreate.addActionListener(e -> mainFrame.showPanel("CreateReservaPanel"));

        JButton btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(e -> mainFrame.showPanel("UpdateReservaPanel"));

        JButton btnRead = new JButton("Leer");
        btnRead.addActionListener(e -> mainFrame.showPanel("ReadReservaPanel"));

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> mainFrame.showPanel("DeleteReservaPanel"));

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("GestiónHR"));

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

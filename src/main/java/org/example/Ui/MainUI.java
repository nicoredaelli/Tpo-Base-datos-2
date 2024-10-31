package org.example.Ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame {
    private JPanel mainPanel;

    public MainUI() {
        setTitle("Hotel Management System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        // Botones CRUD
        JButton createButton = new JButton("Crear");
        JButton readButton = new JButton("Leer");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");

        // Agregar listeners para cambiar a los submenús
        createButton.addActionListener(e -> openSubMenu("Crear"));
        readButton.addActionListener(e -> openSubMenu("Leer"));
        updateButton.addActionListener(e -> openSubMenu("Actualizar"));
        deleteButton.addActionListener(e -> openSubMenu("Eliminar"));

        // Añadir botones al panel principal
        mainPanel.add(createButton);
        mainPanel.add(readButton);
        mainPanel.add(updateButton);
        mainPanel.add(deleteButton);

        add(mainPanel);
        setVisible(true);
    }

    private void openSubMenu(String action) {
        new SubMenuUI(action); // Abre el submenú con el tipo de acción
        this.dispose();
    }

    public static void main(String[] args) {
        new MainUI();
    }
}

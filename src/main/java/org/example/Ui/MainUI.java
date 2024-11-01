package org.example.Ui;

import org.example.conexionmongo.MongoDBConnection;
import org.example.conexionneo4j.Neo4jDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        // Agregar un WindowListener para cerrar las conexiones al salir
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cerrar las conexiones a MongoDB y Neo4j
                MongoDBConnection.closeConnection();
                Neo4jDBConnection.closeConnection();
                System.out.println("Conexiones cerradas correctamente.");
            }
        });

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

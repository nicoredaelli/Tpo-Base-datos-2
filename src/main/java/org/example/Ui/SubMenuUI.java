package org.example.Ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class SubMenuUI extends JFrame {
    private String action;

    public SubMenuUI(String action) {
        this.action = action;
        setTitle(action + " - Seleccione una entidad");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        // Botones para cada entidad
        JButton hotelButton = new JButton("Hotel");
        JButton habitacionButton = new JButton("Habitación");
        JButton huespedButton = new JButton("Huésped");
        JButton reservaButton = new JButton("Reserva");
        JButton amenityButton = new JButton("Amenity");
        JButton zonaButton = new JButton("Zona");
        JButton poiButton = new JButton("Punto de Interés");

        // Añadir listeners para abrir la UI de la entidad correspondiente
        hotelButton.addActionListener(e -> openEntityForm("Hotel"));
        habitacionButton.addActionListener(e -> openEntityForm("Habitación"));
        huespedButton.addActionListener(e -> openEntityForm("Huésped"));
        reservaButton.addActionListener(e -> openEntityForm("Reserva"));
        amenityButton.addActionListener(e -> openEntityForm("Amenity"));
        zonaButton.addActionListener(e -> openEntityForm("Zona"));
        poiButton.addActionListener(e -> openEntityForm("Punto de Interés"));

        // Añadir botones al panel
        panel.add(hotelButton);
        panel.add(habitacionButton);
        panel.add(huespedButton);
        panel.add(reservaButton);
        panel.add(amenityButton);
        panel.add(zonaButton);
        panel.add(poiButton);

        add(panel);
        setVisible(true);
    }

    private void openEntityForm(String entity) {
        new EntityFormUI(action, entity); // Abre el formulario de la entidad
        this.dispose();
    }
}

package org.example.Ui;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.*;
class EntityFormUI extends JFrame {
    private String action;
    private String entity;
    private CRUDController controller;

    public EntityFormUI(String action, String entity) {
        this.action = action;
        this.entity = entity;
        this.controller = new CRUDController(); // Inicializamos el controlador

        setTitle(action + " " + entity);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2)); // Ajusta la cuadrícula según los campos

        // Campos de entrada específicos según la entidad
        JTextField idField = new JTextField(10);
        JTextField nombreField = new JTextField(10);
        JTextField telefonoField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField direccionField = new JTextField(10);
        JTextField zonaField = new JTextField(10);
        
        switch (entity) {
            case "Hotel":
                panel.add(new JLabel("ID Hotel:"));
                panel.add(idField);
                panel.add(new JLabel("Nombre:"));
                panel.add(nombreField);
                panel.add(new JLabel("Teléfono:"));
                panel.add(telefonoField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Dirección:"));
                panel.add(direccionField);
                panel.add(new JLabel("Zona:"));
                panel.add(zonaField);
                break;
            // Añade más campos para otras entidades
            // Similar para otras entidades como Habitacion, Huesped, Reserva, etc.
        }

        JButton submitButton = new JButton(action + " " + entity);
        submitButton.addActionListener(e -> performCRUDAction(idField, nombreField, telefonoField, emailField, direccionField, zonaField));

        panel.add(submitButton);
        add(panel);
        setVisible(true);
    }

    private void performCRUDAction(JTextField idField, JTextField nombreField, JTextField telefonoField, JTextField emailField, JTextField direccionField, JTextField zonaField) {
        try {
            switch (action) {
                case "Crear":
                    if (entity.equals("Hotel")) {
                        // Crear un hotel
                        int idHotel = controller.getUltimoIdHotel(); // Trae el ultimo ID existente de los hoteles (coleccion contadores)
                        idHotel++; // el siguiente del ultimo en uso
                        Hotel hotel = new Hotel(
                                new ObjectId(),
                                idHotel,
                                nombreField.getText(),
                                telefonoField.getText(),
                                emailField.getText(),
                                Map.of("direccion", direccionField.getText()),
                                List.of(),
                                Integer.parseInt(zonaField.getText())
                        );
                        controller.createHotel(hotel);

                    }
                    // Repite para otras entidades como Habitacion, Huesped, etc.
                    break;

                case "Leer":
                    if (entity.equals("Hotel")) {
                        // Leer un hotel por ID
                        int idHotel = Integer.parseInt(idField.getText());
                        Hotel hotel = controller.readHotel(idHotel);
                        JOptionPane.showMessageDialog(this, hotel != null ? hotel.toString() : "Hotel no encontrado");
                    }
                    // Repite para otras entidades
                    break;

                case "Actualizar":
                    if (entity.equals("Hotel")) {
                        // Actualizar nombre de un hotel
                        ObjectId idHotel = new ObjectId(idField.getText());
                        controller.updateHotel(idHotel, nombreField.getText());
                    }
                    // Repite para otras entidades
                    break;

                case "Eliminar":
                    if (entity.equals("Hotel")) {
                        // Eliminar un hotel por ID
                        ObjectId idHotel = new ObjectId(idField.getText());
                        controller.deleteHotel(idHotel);
                    }
                    // Repite para otras entidades
                    break;
            }
            JOptionPane.showMessageDialog(this, entity + " " + action + " exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        new MainUI(); // Volver al menú principal después de la acción
        dispose();
    }
}

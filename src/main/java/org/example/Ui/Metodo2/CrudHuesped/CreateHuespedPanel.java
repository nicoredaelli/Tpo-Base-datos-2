package org.example.Ui.Metodo2.CrudHuesped;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;


public class CreateHuespedPanel extends JPanel {
    private JTextField nombreField, apellidoField, telefonoField, emailField, calleField, numeroField, provinciaField, paisField;
    private CRUDController crudController;

    public CreateHuespedPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nombreField = new JTextField(20);
        apellidoField = new JTextField(20);
        telefonoField = new JTextField(20);
        emailField = new JTextField(20);
        calleField = new JTextField(20);
        numeroField = new JTextField(5);
        provinciaField = new JTextField(20);
        paisField = new JTextField(20);

        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Apellido:"));
        add(apellidoField);
        add(new JLabel("Teléfono:"));
        add(telefonoField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Calle:"));
        add(calleField);
        add(new JLabel("Número:"));
        add(numeroField);
        add(new JLabel("Provincia:"));
        add(provinciaField);
        add(new JLabel("País:"));
        add(paisField);

        JButton createButton = new JButton("Crear Huésped");
        createButton.addActionListener(e -> crearHuesped());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HuespedCRUDPanel"));

        add(createButton);
        add(backButton);
    }

    private void crearHuesped() {
        int idHuesped = crudController.getUltimoIdHuesped() + 1;
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", calleField.getText());
        direccion.put("numero", numeroField.getText());
        direccion.put("provincia", provinciaField.getText());
        direccion.put("pais", paisField.getText());

        Huesped nuevoHuesped = new Huesped(
                new ObjectId(),
                idHuesped,
                nombreField.getText(),
                apellidoField.getText(),
                telefonoField.getText(),
                emailField.getText(),
                direccion
        );

        crudController.createHuesped(nuevoHuesped);
        JOptionPane.showMessageDialog(this, "Huésped creado exitosamente.");
    }
}

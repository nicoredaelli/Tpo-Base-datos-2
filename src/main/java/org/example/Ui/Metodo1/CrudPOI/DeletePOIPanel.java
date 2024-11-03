package org.example.Ui.Metodo1.CrudPOI;

import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;

public class DeletePOIPanel extends JPanel {
    private JTextField idField;
    private JButton deleteButton, backButton;
    private CRUDController crudController;

    public DeletePOIPanel(MainFrame mainFrame) {
        crudController = new CRUDController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        idField = new JTextField(10);
        add(new JLabel("ID del Punto de Interés a eliminar:"));
        add(idField);

        deleteButton = new JButton("Eliminar POI");
        deleteButton.addActionListener(e -> eliminarPOI());
        add(deleteButton);

        backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("POICRUDPanel")); // Ajusta el nombre del panel principal si es necesario
        add(backButton);
    }

    private void eliminarPOI() {
        
            int idPoi = Integer.parseInt(idField.getText());
            
            // Llamada al controlador para eliminar el POI
            crudController.deletePuntoDeInteres(idPoi);

            
    }
}

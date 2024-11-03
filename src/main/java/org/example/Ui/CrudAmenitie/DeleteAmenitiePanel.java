package org.example.Ui.CrudAmenitie;


import javax.swing.*;
import java.awt.*;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;

public class DeleteAmenitiePanel extends JPanel {
    private JTextField idField;
    private CRUDController crudController;

    public DeleteAmenitiePanel(MainFrame mainFrame) {
        // Inicializar el controlador CRUD
        this.crudController = new CRUDController();
        
        // Configuración del diseño
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Crear y agregar componentes de la interfaz
        JLabel idLabel = new JLabel("ID del Amenity a Eliminar:");
        idField = new JTextField(10); // Limita el tamaño del campo
        JButton deleteButton = new JButton("Eliminar");
        JButton backButton = new JButton("Regresar");

        // Acción del botón Eliminar
        deleteButton.addActionListener(e -> eliminarAmenity());

        // Acción del botón Regresar
        backButton.addActionListener(e -> mainFrame.showPanel("AmenityCRUDPanel"));

        // Añadir componentes al panel
        add(idLabel);
        add(idField);
        add(deleteButton);
        add(backButton);

        // Establecer alineación central de los componentes
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void eliminarAmenity() {

        // Obtener el ID ingresado y convertirlo en número
        int idAmenity = Integer.parseInt(idField.getText());

        // Llamar al método de eliminación en el controlador
        crudController.deleteAmenity(idAmenity);
        
    }
}


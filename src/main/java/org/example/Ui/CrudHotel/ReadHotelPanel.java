package org.example.Ui.CrudHotel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.types.ObjectId;
import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

public class ReadHotelPanel extends JPanel {
    
        private JTextField idField;
    
        CRUDController crudController = new CRUDController();
    
        public ReadHotelPanel(MainFrame mainFrame) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
            idField = new JTextField();
            idField.setPreferredSize(new Dimension(10, 30)); // Establecer un tamaño preferido
    
            add(new JLabel("Id del hotel:"));
            add(idField);
    
            JButton deleteButton = new JButton("Leer");
            deleteButton.addActionListener(e -> LeerHotel());
    
            JButton backButton = new JButton("Regresar");
            backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));
    
            add(deleteButton);
            add(backButton);
        }
    
        private void LeerHotel() {
            int id = Integer.parseInt(idField.getText());
            crudController.readHotel(id);
           
                
            
        }       
        
    }
package org.example.Ui.Metodo1.CrudHotel;


import java.util.List;

import org.example.Ui.MainFrame;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import javax.swing.*;

public class ReadHotelPanel extends JPanel {
    
    private JComboBox<String> hotelDropdown; // Cuadro desplegable para seleccionar el hotel
    private JTextArea hotelDetailsArea; // Área para mostrar los detalles del hotel
    private CRUDController crudController = new CRUDController();
    private List<Hotel> hotelesDisponibles; // Lista de hoteles disponibles

    public ReadHotelPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Obtener la lista de hoteles disponibles desde el CRUDController
        hotelesDisponibles = crudController.getAllHoteles();

        // Crear el cuadro desplegable con los nombres de los hoteles
        hotelDropdown = new JComboBox<>(hotelesDisponibles.stream()
                .map(Hotel::getNombre)
                .toArray(String[]::new));
        
        add(new JLabel("Seleccione un hotel:"));
        add(hotelDropdown);

        JButton readButton = new JButton("Cargar Datos");
        readButton.addActionListener(e -> cargarDatos());

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> mainFrame.showPanel("HotelCRUDPanel"));

        add(readButton);
        
        // Configuración del área de texto para mostrar los detalles del hotel
        hotelDetailsArea = new JTextArea(10, 30);
        hotelDetailsArea.setEditable(false); // Hacer que el área de texto no sea editable
        add(new JScrollPane(hotelDetailsArea)); // Añadir un scroll para el área de texto

        add(backButton);
    }

    private void cargarDatos() {
        String nombreHotel = (String) hotelDropdown.getSelectedItem();

        // Buscar el hotel seleccionado
        Hotel hotel = hotelesDisponibles.stream()
                .filter(h -> h.getNombre().equals(nombreHotel))
                .findFirst()
                .orElse(null);

        if (hotel != null) {
            // Formatear la información del hotel para que sea más legible
            StringBuilder detalles = new StringBuilder();
            detalles.append("Detalles del Hotel:\n");
            detalles.append("ID: ").append(hotel.getIdHotel()).append("\n");
            detalles.append("Nombre: ").append(hotel.getNombre()).append("\n");
            detalles.append("Teléfono: ").append(hotel.getTelefono()).append("\n");
            detalles.append("Email: ").append(hotel.getEmail()).append("\n");
            
            detalles.append("Dirección: ").append(hotel.getDireccion()).append("\n");
            
            detalles.append("Habitaciones: ").append(hotel.getHabitaciones().size()).append("\n"); // Si tienes la lista de habitaciones

            // Mostrar los detalles del hotel en el área de texto
            hotelDetailsArea.setText(detalles.toString());
        } else {
            hotelDetailsArea.setText("Hotel no encontrado.");
        }
    }
}


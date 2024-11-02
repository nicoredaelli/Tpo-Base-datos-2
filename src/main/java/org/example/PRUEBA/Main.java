package org.example.PRUEBA;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Inicializar el controlador CRUD
        CRUDController crudController = new CRUDController();

        // Crear una dirección ficticia para el hotel
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", "Calle Falsa 123");
        direccion.put("ciudad", "Buenos Aires");
        direccion.put("codigoPostal", "1000");

        // Crear una lista de IDs de habitaciones (esto es un ejemplo de habitaciones disponibles)
        List<Integer> habitaciones = Arrays.asList(101, 102, 103);

        // Crear un nuevo hotel con datos de prueba
        Hotel nuevoHotel = new Hotel(
            new ObjectId(),        // Generar un nuevo ObjectId
            1,                     // idHotel, por ejemplo
            "Hotel Ejemplo",       // Nombre del hotel
            "+541123456789",       // Teléfono
            "contacto@hotel.com",  // Email
            direccion,             // Dirección
            habitaciones,          // Habitaciones
            1                      // ID de la zona
        );

        // Intentar crear el hotel en MongoDB y Neo4j
        System.out.println("Intentando crear el hotel: " + nuevoHotel);
        crudController.createHotel(nuevoHotel);
        System.out.println("Hotel creado exitosamente.");

        // Leer el hotel de MongoDB para verificar que se guardó correctamente
        Hotel hotelGuardado = crudController.readHotel(1);
        if (hotelGuardado != null) {
            System.out.println("Hotel recuperado de MongoDB: " + hotelGuardado);
        } else {
            System.out.println("No se encontró el hotel en MongoDB.");
        }
    }
}

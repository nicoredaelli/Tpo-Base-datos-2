package org.example.PRUEBA;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelCrudTest {
    public static void main(String[] args) throws InterruptedException {
        // Inicializar el controlador CRUD
        CRUDController crudController = new CRUDController();

        // Crear una dirección ficticia para el hotel
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", "Calle Falsa");
        direccion.put("numero", "24");
        direccion.put("codigo_postal", "C2345");
        direccion.put("provincia", "Buenos Aires");
        direccion.put("pais", "Argentina");

        // Crear una lista de IDs de habitaciones (esto es un ejemplo de habitaciones disponibles)
        List<Integer> habitaciones = Arrays.asList(101, 102, 103);

        int idHotel = crudController.getUltimoIdHotel(); // Trae el ultimo ID existente de los hoteles (coleccion contadores)
        idHotel++; // el siguiente del ultimo en uso

        // Crear un nuevo hotel con datos de prueba
        Hotel nuevoHotel = new Hotel(
            new ObjectId(),        // Generar un nuevo ObjectId
                idHotel,                     // idHotel, por ejemplo
            "hotelcitoooo",       // Nombre del hotel
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
        Hotel hotelGuardado = crudController.readHotel(idHotel);
        if (hotelGuardado != null) {
            System.out.println("Hotel recuperado de MongoDB: " + hotelGuardado);
        } else {
            System.out.println("No se encontró el hotel en MongoDB.");
        }

        System.out.println("Ahora, se va a actualizar el hotel con otros datos ...");

        String nuevoNombre = "Hotel Actualizado";
        String nuevoTelefono = "123-456-789";

        // Crear una dirección ficticia para el hotel
        Map<String, String> nuevaDireccion = new HashMap<>();
        nuevaDireccion.put("calle", "Av. Sarasa");
        nuevaDireccion.put("numero", "1234");
        nuevaDireccion.put("codigo_postal", "C2000");
        nuevaDireccion.put("provincia", "Misiones");
        nuevaDireccion.put("pais", "Argentina");

        String nuevoEmail = "contacto@hotelba.com";
        int nuevaZona = 4;

        // Llamada al metodo para actualizar los datos del hotel
        crudController.updateHotel(idHotel, nuevoNombre, nuevoTelefono, nuevaDireccion, nuevoEmail, habitaciones, nuevaZona);

        System.out.println("Hotel actualizado con éxito.");

        // Leer el hotel de MongoDB para verificar que se actualizo correctamente
        Hotel hotelActualizado = crudController.    readHotel(idHotel);

        if (hotelActualizado != null) {
            System.out.println("Hotel actualizado recuperado de MongoDB: " + hotelActualizado);
        } else {
            System.out.println("No se encontró el hotel actualizado en MongoDB.");
        }


        // Eliminar el hotel de MongoDB
        crudController.deleteHotel(idHotel);
        System.out.println("Hotel eliminado de MongoDB con id_hotel: " + idHotel);

        // Verificar que el hotel fue eliminado de MongoDB
        Hotel hotelEliminadoMongo = crudController.readHotel(idHotel);
        if (hotelEliminadoMongo == null) {
            System.out.println("Confirmación: el hotel con id_hotel " + idHotel + " ya no existe en MongoDB.");
        } else {
            System.out.println("Error: el hotel aún existe en MongoDB.");
        }

    }
}

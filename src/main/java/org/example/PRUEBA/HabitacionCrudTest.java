package org.example.PRUEBA;

import org.example.controlador.CRUDController;
import org.example.entidades.Habitacion;

import java.util.Arrays;
import java.util.List;

public class HabitacionCrudTest {
    public static void main(String[] args) {
        // Inicializar el controlador CRUD
        CRUDController crudController = new CRUDController();

        // Crear una lista de IDs de amenities (esto es un ejemplo de amenities disponibles)
        List<Integer> amenities = Arrays.asList(1, 2, 3);

        int idHabitacion = crudController.getUltimoIDHabitacion() + 1;

        // Crear una nueva habitación con datos de prueba
        Habitacion nuevaHabitacion = new Habitacion(

                idHabitacion,                  // Número de la habitación
                12,
                1,                 // ID del hotel (generar un nuevo ObjectId)
                "Deluxe",                       // Tipo de habitación
                amenities                       // Lista de amenities
        );

        // Intentar crear la habitación en MongoDB y Neo4j
        System.out.println("Intentando crear la habitación: " + nuevaHabitacion);
        crudController.createHabitacion(nuevaHabitacion);
        System.out.println("Habitación creada exitosamente.");

        // Leer la habitación de MongoDB para verificar que se guardó correctamente
        Habitacion habitacionGuardada = crudController.readHabitacion(nuevaHabitacion.getNroHabitacion());
        if (habitacionGuardada != null) {
            System.out.println("Habitación recuperada de MongoDB: " + habitacionGuardada);
        } else {
            System.out.println("No se encontró la habitación en MongoDB.");
        }

        System.out.println("Ahora, se va a actualizar la habitación con otros datos ...");

        // Actualizar la habitación con nuevos datos
        nuevaHabitacion.setTipoHabitacion("Super Suite");
        nuevaHabitacion.setAmenities(Arrays.asList(4, 5, 6));

        // Llamada al metodo para actualizar los datos de la habitación
        crudController.updateHabitacion(nuevaHabitacion);
        System.out.println("Habitación actualizada con éxito.");

        // Leer la habitación de MongoDB para verificar que se actualizó correctamente
        Habitacion habitacionActualizada = crudController.readHabitacion(nuevaHabitacion.getNroHabitacion());
        if (habitacionActualizada != null) {
            System.out.println("Habitación actualizada recuperada de MongoDB: " + habitacionActualizada);
        } else {
            System.out.println("No se encontró la habitación actualizada en MongoDB.");
        }


        // Eliminar la habitación de MongoDB
        crudController.deleteHabitacion(nuevaHabitacion.getNroHabitacion());
        System.out.println("Habitación eliminada de MongoDB con idHabitacion: " + nuevaHabitacion.getNroHabitacion());

        // Verificar que la habitación fue eliminada de MongoDB
        Habitacion habitacionEliminadaMongo = crudController.readHabitacion(nuevaHabitacion.getNroHabitacion());
        if (habitacionEliminadaMongo == null) {
            System.out.println("Confirmación: la habitación con idHabitacion " + nuevaHabitacion.getNroHabitacion() + " ya no existe en MongoDB.");
        } else {
            System.out.println("Error: la habitación aún existe en MongoDB.");
        }
    }
}
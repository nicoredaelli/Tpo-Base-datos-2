package org.example.PRUEBA;

import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;

public class AmenityTest {
    public static void main(String[] args) {
        // Instancia del controlador CRUD
        CRUDController controller = new CRUDController();

        // Crear un nuevo amenity
        Amenity newAmenity = new Amenity(1, "Piscina", "Piscina climatizada en la azotea");
        System.out.println("Creando Amenity...");
        controller.createAmenity(newAmenity);

        // Leer el amenity creado
        System.out.println("\nLeyendo Amenity creado:");
        Amenity retrievedAmenity = controller.readAmenity(newAmenity.getIdAmenity());
        if (retrievedAmenity != null) {
            System.out.println("Amenity encontrado: " + retrievedAmenity.getNombre() + " - " + retrievedAmenity.getDescripcion());
        } else {
            System.out.println("No se encontró el amenity en la base de datos.");
        }

        // Actualizar el amenity
        System.out.println("\nActualizando Amenity...");
        retrievedAmenity.setNombre("Piscina Exterior");
        retrievedAmenity.setDescripcion("Piscina con vista panorámica");
        controller.updateAmenity(retrievedAmenity);

        // Leer el amenity actualizado
        System.out.println("\nLeyendo Amenity actualizado:");
        Amenity updatedAmenity = controller.readAmenity(retrievedAmenity.getIdAmenity());
        if (updatedAmenity != null) {
            System.out.println("Amenity actualizado: " + updatedAmenity.getNombre() + " - " + updatedAmenity.getDescripcion());
        } else {
            System.out.println("No se encontró el amenity en la base de datos después de la actualización.");
        }

        // Eliminar el amenity
        System.out.println("\nEliminando Amenity...");
        controller.deleteAmenity(retrievedAmenity.getIdAmenity());

        // Intentar leer el amenity eliminado
        System.out.println("\nVerificando eliminación del Amenity:");
        Amenity deletedAmenity = controller.readAmenity(retrievedAmenity.getIdAmenity());
        if (deletedAmenity == null) {
            System.out.println("Amenity eliminado exitosamente.");
        } else {
            System.out.println("Error: el amenity todavía existe en la base de datos.");
        }
    }
}

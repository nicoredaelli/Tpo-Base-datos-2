package org.example.PRUEBA;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Amenity;

import java.util.List;

public class AmenityTest {
    public static void main(String[] args) {
        CRUDController amenityCRUD = new CRUDController();

        int idAmenity = amenityCRUD.getUltimoIdAmenity() + 1;

        // Crear un nuevo Amenity
        System.out.println("Creando un nuevo Amenity...");
        Amenity nuevoAmenity = new Amenity(new ObjectId(), idAmenity, "cama doble", "cama doble");
        amenityCRUD.createAmenity(nuevoAmenity);
        System.out.println("Amenity creado: " + nuevoAmenity.getNombre());

        // Leer el Amenity creado
        System.out.println("\nLeyendo el Amenity creado...");
        Amenity amenityLeido = amenityCRUD.readAmenity(nuevoAmenity.getIdAmenity());
        if (amenityLeido != null) {
            System.out.println("Amenity leído: " + amenityLeido.toString());
        } else {
            System.out.println("Amenity no encontrado.");
        }

        // Actualizar el Amenity
        System.out.println("\nActualizando el Amenity...");
        nuevoAmenity.setDescripcion("pepe");
        amenityCRUD.updateAmenity(nuevoAmenity);
        System.out.println("Amenity actualizado.");

        // Eliminar el Amenity
        System.out.println("\nEliminando el Amenity...");
        amenityCRUD.deleteAmenity(nuevoAmenity.getIdAmenity());
        System.out.println("Amenity eliminado.");

    }
}

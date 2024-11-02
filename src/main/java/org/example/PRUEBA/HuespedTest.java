package org.example.PRUEBA;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;

public class HuespedTest{
    public static void main(String[] args) {
        // Instancia del controlador CRUD
        CRUDController controller = new CRUDController();

        // Crear una dirección para el huésped
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", "Av. Corrientes");
        direccion.put("numero", "1234");
        direccion.put("ciudad", "Buenos Aires");
        direccion.put("codigoPostal", "C1043AAB");

        // Crear un nuevo huésped
        Huesped newHuesped = new Huesped(new ObjectId(), "Juan", "Pérez", "123456789", "juan.perez@example.com", direccion);
        System.out.println("Creando Huesped...");
        controller.createHuesped(newHuesped);

        // Leer el huésped creado
        System.out.println("\nLeyendo Huesped creado:");
        Huesped retrievedHuesped = controller.readHuesped(newHuesped.getIdHuesped());
        if (retrievedHuesped != null) {
            System.out.println("Huesped encontrado: " + retrievedHuesped.getNombre() + " " + retrievedHuesped.getApellido() + " - " + retrievedHuesped.getTelefono());
        } else {
            System.out.println("No se encontró el huesped en la base de datos.");
        }

        // Actualizar el huésped
        System.out.println("\nActualizando Huesped...");
        retrievedHuesped.setTelefono("987654321");
        controller.updateHuesped(retrievedHuesped);

        // Leer el huésped actualizado
        System.out.println("\nLeyendo Huesped actualizado:");
        Huesped updatedHuesped = controller.readHuesped(retrievedHuesped.getIdHuesped());
        if (updatedHuesped != null) {
            System.out.println("Huesped actualizado: " + updatedHuesped.getNombre() + " " + updatedHuesped.getApellido() + " - " + updatedHuesped.getTelefono());
        } else {
            System.out.println("No se encontró el huesped en la base de datos después de la actualización.");
        }

        // Eliminar el huésped
        System.out.println("\nEliminando Huesped...");
        controller.deleteHuesped(retrievedHuesped.getIdHuesped());

        // Verificar la eliminación del huésped
        System.out.println("\nVerificando eliminación del Huesped:");
        Huesped deletedHuesped = controller.readHuesped(retrievedHuesped.getIdHuesped());
        if (deletedHuesped == null) {
            System.out.println("Huesped eliminado exitosamente.");
        } else {
            System.out.println("Error: el huesped todavía existe en la base de datos.");
        }
    }
}

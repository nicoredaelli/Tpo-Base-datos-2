package org.example.PRUEBA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Huesped;

public class HuespedTest{
    public static void main(String[] args) {
        CRUDController huespedCRUD = new CRUDController();

        // Crear un nuevo Huesped
        System.out.println("Creando un nuevo Huésped...");
        Map<String, String> direccion = new HashMap<>();
        direccion.put("calle", "Calle Falsa");
        direccion.put("numero", "742");
        direccion.put("provincia", "Springfield");
        direccion.put("pais", "USA");

        int idHuesped = huespedCRUD.getUltimoIdHuesped() + 1;

        Huesped nuevoHuesped = new Huesped(
                new ObjectId(),
                idHuesped,
                "Homero",
                "Simpson",
                "555-1234",
                "homero.simpson@mail.com",
                direccion);
        huespedCRUD.createHuesped(nuevoHuesped);
        System.out.println("Huésped creado: " + nuevoHuesped.toString());

        // Leer el Huesped creado
        System.out.println("\nLeyendo el Huésped creado...");
        Huesped huespedLeido = huespedCRUD.readHuesped(nuevoHuesped.getIdHuesped());
        if (huespedLeido != null) {
            System.out.println("Huésped leído: " + huespedLeido.toString());
        } else {
            System.out.println("Huésped no encontrado.");
        }

        // Actualizar el Huesped
        System.out.println("\nActualizando el Huésped...");
        nuevoHuesped.setTelefono("555-5678");
        nuevoHuesped.setEmail("homero.simpson@newmail.com");
        huespedCRUD.updateHuesped(nuevoHuesped);
        System.out.println("Huésped actualizado.");

        // Verificar la actualización
        Huesped huespedActualizado = huespedCRUD.readHuesped(nuevoHuesped.getIdHuesped());
        System.out.println("Teléfono actualizado del Huésped: " + huespedActualizado.toString());



        // Eliminar el Huesped
        System.out.println("\nEliminando el Huésped...");
        huespedCRUD.deleteHuesped(nuevoHuesped.getIdHuesped());
        System.out.println("Huésped eliminado.");

        // Verificar eliminación
        Huesped huespedEliminado = huespedCRUD.readHuesped(nuevoHuesped.getIdHuesped());
        if (huespedEliminado == null) {
            System.out.println("Confirmación: Huésped eliminado exitosamente.");
        } else {
            System.out.println("Error: El Huésped aún existe.");
        }
    }

}

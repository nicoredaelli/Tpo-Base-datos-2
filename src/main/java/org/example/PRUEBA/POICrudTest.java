package org.example.PRUEBA;

import org.bson.types.ObjectId;
import org.example.controlador.CRUDController;
import org.example.entidades.Hotel;
import org.example.entidades.PuntoDeInteres;

import java.util.HashMap;
import java.util.Map;

public class POICrudTest {
    public static void main(String[] args) {
        // Inicializar el controlador CRUD
        CRUDController crudController = new CRUDController();

        int idPoi = crudController.getUltimoIdPuntoDeInteres();
        idPoi++; // el siguiente del ultimo en uso
        // Crear un nuevo punto de interes con datos de prueba
        PuntoDeInteres nuevoPuntoDeInteres = new PuntoDeInteres(
                new ObjectId(),        // Generar un nuevo ObjectId
                idPoi,
                "PoiPrueba",       // Nombre del punto de interes
                "poiPrueba.",  // Descripcion
                1  // ID de la zona
        );

        // Intentar crear el punto de interes en MongoDB y Neo4j
        System.out.println("Intentando crear el punto de interes: " + nuevoPuntoDeInteres);
        crudController.createPuntoDeInteres(nuevoPuntoDeInteres);
        System.out.println("Punto de interes creado exitosamente.");

        // Leer el punto de interes de MongoDB para verificar que se guardó correctamente
        PuntoDeInteres puntoDeInteresGuardado = crudController.readPuntoDeInteres(idPoi);
        if (puntoDeInteresGuardado != null) {
            System.out.println("Punto de interes recuperado de MongoDB: " + puntoDeInteresGuardado);
        } else {
            System.out.println("No se encontró el punto de interes en MongoDB.");
        }

        System.out.println("Ahora, se va a actualizar el punto de interes con otros datos ...");

        String nuevoNombre = "PoiPruebaActualizado";
        String nuevaDescripcion = "Desc PoiPruebaActualizado";
        int nuevaZona = 4;

        // Llamada al metodo para actualizar los datos del hotel
        crudController.updatePuntoDeInteres(idPoi, nuevoNombre, nuevaDescripcion, nuevaZona);

        System.out.println("Punto de interes actualizado con éxito.");

        // Leer el hotel de MongoDB para verificar que se actualizo correctamente
        PuntoDeInteres puntoDeInteresActualizado = crudController.readPuntoDeInteres(idPoi);

        if (puntoDeInteresActualizado != null) {
            System.out.println("Punto de interes actualizado recuperado de MongoDB: " + puntoDeInteresActualizado);
        } else {
            System.out.println("No se encontró el punto de interes actualizado en MongoDB.");
        }

        // Eliminar el punto de interes de MongoDB
        crudController.deletePuntoDeInteres(idPoi);
        System.out.println("Punto de interes eliminado de MongoDB con id_poi: " + idPoi);

        // Verificar que el punto de interes fue eliminado de MongoDB
        PuntoDeInteres puntoDeInteresEliminado = crudController.readPuntoDeInteres(idPoi);
        if (puntoDeInteresEliminado == null) {
            System.out.println("Confirmación: el punto de interes con id_poi " + idPoi + " ya no existe en MongoDB.");
        } else {
            System.out.println("Error: el punto de interes aún existe en MongoDB.");
        }

    }
}

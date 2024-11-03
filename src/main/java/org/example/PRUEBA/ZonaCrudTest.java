package org.example.PRUEBA;

import org.example.controlador.CRUDController;
import org.example.entidades.Zona;

public class ZonaCrudTest {
    public static void main(String[] args) {
        // Inicializar el controlador CRUD para zona
        CRUDController crudController = new CRUDController();

        int idZona = crudController.getUltimoIDZona() + 1;

        // Crear una nueva zona con datos de ejemplo
        Zona nuevaZona = new Zona(
                idZona,                         // idZona
                "Microcentro",                  // nombre
                "CABA",            // provincia
                "Argentina",               // pais
                "Zona céntrica y comercial" // descripcion
        );

        // Crear la zona en MongoDB
        crudController.createZona(nuevaZona);
        System.out.println("Zona creada exitosamente.");

        // Leer la zona recién creada para verificar
        Zona zonaCreada = crudController.readZona(nuevaZona.getIdZona());
        if (zonaCreada != null) {
            System.out.println("Zona recuperada de MongoDB: ");
            System.out.println("ID Zona: " + zonaCreada.getIdZona());
            System.out.println("Nombre: " + zonaCreada.getNombre());
            System.out.println("Provincia: " + zonaCreada.getProvincia());
            System.out.println("País: " + zonaCreada.getPais());
            System.out.println("Descripción: " + zonaCreada.getDescripcion());
        } else {
            System.out.println("No se encontró la zona en MongoDB.");
        }
    }
}

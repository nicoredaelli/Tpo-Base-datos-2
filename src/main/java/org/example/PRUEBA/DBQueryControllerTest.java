package org.example.PRUEBA;

import org.example.conexionmongo.MongoDBConnection;
import org.example.conexionneo4j.Neo4jDBConnection;
import org.example.controlador.DatabaseQueryController;
import org.example.entidades.Hotel;
import org.example.entidades.PuntoDeInteres;

import java.util.List;

public class DBQueryControllerTest {

    public static void main(String[] args) {
        DatabaseQueryController controller = new DatabaseQueryController();

        int idHotel = 28;
        List<PuntoDeInteres> puntosDeInteres = controller.getPOIsByIDHotel(idHotel);

        if (puntosDeInteres != null && !puntosDeInteres.isEmpty()) {
            System.out.println("Puntos de Interés encontrados para Hotel: " + idHotel);
            for (PuntoDeInteres poi : puntosDeInteres) {
                System.out.println("ID: " + poi.getIdPoi());
                System.out.println("Nombre: " + poi.getNombre());
                System.out.println("Descripción: " + poi.getDescripcion());
                System.out.println("Zona ID: " + poi.getZona());
                System.out.println("-------------");
            }
        } else {
            System.out.println("No se encontraron puntos de interés para el hotel con ID: " + idHotel);
        }

        int idPoi = 5;
        List<Hotel> hoteles = controller.getHotelesByIDPOI(idPoi);

        if (hoteles != null && !hoteles.isEmpty()) {
            System.out.println("Hoteles encontrados para Punto de interes: " + idPoi);
            for (Hotel hotel : hoteles) {
                System.out.println("ID: " + hotel.getIdHotel());
                System.out.println("Nombre: " + hotel.getNombre());
                System.out.println("Teléfono: " + hotel.getTelefono());
                System.out.println("Email: " + hotel.getEmail());
                System.out.println("Direccion: " + hotel.getDireccion().toString());
                System.out.println("Zona ID: " + hotel.getZona());
                System.out.println("-------------");
            }
        } else {
            System.out.println("No se encontraron hoteles para el punto de interes con ID: " + idPoi);
        }

        MongoDBConnection.closeConnection();
        Neo4jDBConnection.closeConnection();

    }
}

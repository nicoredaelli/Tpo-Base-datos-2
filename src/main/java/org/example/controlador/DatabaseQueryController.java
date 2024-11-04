package org.example.controlador;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import io.mateu.dtos.Crud;
import org.example.conexionmongo.MongoDBConnection;
import org.example.conexionneo4j.Neo4jDBConnection;
import org.example.entidades.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Record;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseQueryController {
    private final MongoDatabase mongoDB;
    private final Driver neo4jDB;
    private final CRUDController crudController;

    public DatabaseQueryController() {
        this.mongoDB = MongoDBConnection.getDatabase();
        this.neo4jDB = Neo4jDBConnection.getConnection();
        this.crudController = new CRUDController();
    }

    public List<PuntoDeInteres> getPOIsByIDHotel(int idHotel) {
        List<PuntoDeInteres> puntosDeInteres = new ArrayList<>();

        try (Session session = neo4jDB.session()) {
            // Paso 1: Obtener solo los IDs de los puntos de interés relacionados con el hotel en Neo4j
            Result result = session.run(
                    "MATCH (p:poi)-[:PERTENECE]->(z:zona)<-[:PERTENECE]-(h:hotel {id_hotel: $idHotel}) " +
                            "RETURN p.id_poi AS id_poi",
                    Map.of("idHotel", idHotel)
            );

            // Recoger todos los ids de puntos de interés en una lista
            List<Integer> poiIds = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                poiIds.add(record.get("id_poi").asInt());
            }

            // Paso 2: Buscar los detalles de cada punto de interés en MongoDB usando los IDs
            MongoCollection<Document> collection = mongoDB.getCollection("pois");
            for (Integer poiId : poiIds) {
                Document doc = collection.find(Filters.eq("id_poi", poiId)).first();
                if (doc != null) {
                    PuntoDeInteres poi = new PuntoDeInteres(
                            doc.getObjectId("_id"),
                            doc.getInteger("id_poi"),
                            doc.getString("nombre"),
                            doc.getString("descripcion"),
                            doc.getInteger("zona")
                    );
                    puntosDeInteres.add(poi);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener puntos de interés por ID de hotel: " + e.getMessage());
        }
        return puntosDeInteres;
    }

    public List<Hotel> getHotelesByIDPOI(int idPoi) {
        List<Hotel> hoteles = new ArrayList<>();

        try (Session session = neo4jDB.session()) {
            // Paso 1: Obtener solo los IDs de los hoteles relacionados con el punto de interés en Neo4j
            Result result = session.run(
                    "MATCH (h:hotel)-[:PERTENECE]->(z:zona)<-[:PERTENECE]-(p:poi {id_poi: $idPoi}) " +
                            "RETURN h.id_hotel AS id_hotel",
                    Map.of("idPoi", idPoi)
            );

            // Recoger todos los ids de hoteles en una lista
            List<Integer> hotelIds = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                hotelIds.add(record.get("id_hotel").asInt());
            }

            // Paso 2: Buscar los detalles de cada hotel en MongoDB usando los IDs
            MongoCollection<Document> collection = mongoDB.getCollection("hoteles");
            for (Integer hotelId : hotelIds) {
                Document doc = collection.find(Filters.eq("id_hotel", hotelId)).first();
                if (doc != null) {
                    Hotel hotel = new Hotel(
                            doc.getObjectId("_id"),
                            doc.getInteger("id_hotel"),
                            doc.getString("nombre"),
                            doc.getString("telefono"),
                            doc.getString("email"),
                            (Map<String, String>) doc.get("direccion"),   // Casteo a `Map` para la dirección
                            (List<Integer>) doc.get("habitaciones"),      // Casteo a `List` para las habitaciones
                            doc.getInteger("zona")
                    );
                    hoteles.add(hotel);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener hoteles por ID de punto de interés: " + e.getMessage());
        }
        return hoteles;
    }

    public List<Amenity> getAmenitiesByHabitacion(int nroHabitacion) {
        List<Amenity> amenities = new ArrayList<>();

        try (Session session = neo4jDB.session()) {
            // Paso 1: Obtener los IDs de los amenities relacionados con la habitación en Neo4j
            Result result = session.run(
                        "MATCH (h:habitacion{nro_habitacion:$nroHabitacion})-[:TIENE_AMENITY]->(a:amenity) " +
                                "RETURN a.id_amenity AS id_amenity",
                    Map.of("nroHabitacion", nroHabitacion)
            );

            // Recoger todos los ids de amenities en una lista
            List<Integer> amenityIds = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                amenityIds.add(record.get("id_amenity").asInt());
            }

            // Paso 2: Buscar los detalles de cada amenity en MongoDB usando los IDs
            MongoCollection<Document> collection = mongoDB.getCollection("amenities");
            for (Integer amenityId : amenityIds) {
                Document doc = collection.find(Filters.eq("id_amenity", amenityId)).first();
                if (doc != null) {
                    Amenity amenity = new Amenity(
                            doc.getObjectId("_id"),
                            doc.getInteger("id_amenity"),
                            doc.getString("nombre"),
                            doc.getString("descripcion")
                    );
                    amenities.add(amenity);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener amenities por número de habitación: " + e.getMessage());
        }

        return amenities;
    }

    public List<Reserva> findReservasByHuesped(int idHuesped) {
        MongoCollection<Document> collection = mongoDB.getCollection("reservas");
        List<Document> reservasDocs = collection.find(Filters.eq("id_huesped", idHuesped)).into(new ArrayList<>());

        List<Reserva> reservas = new ArrayList<>();
        for (Document doc : reservasDocs) {
            try {
                Reserva reserva = new Reserva(
                        doc.getInteger("cod_reserva"),
                        doc.getDate("checkin"),
                        doc.getDate("checkout"),
                        EstadoReserva.valueOf(doc.getString("estado_reserva")),
                        doc.getDouble("tarifa"),
                        doc.getInteger("id_hotel"),
                        doc.getInteger("id_habitacion"),
                        doc.getInteger("id_huesped")
                );
                reservas.add(reserva);
            } catch (Exception e) {
                System.err.println("Error al mapear reserva: " + e.getMessage());
            }
        }
        return reservas;
    }

}


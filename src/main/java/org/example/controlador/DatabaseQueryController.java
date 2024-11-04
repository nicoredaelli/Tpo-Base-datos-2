package org.example.controlador;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import io.mateu.dtos.Crud;
import org.bson.types.ObjectId;
import org.example.conexionmongo.MongoDBConnection;
import org.example.conexionneo4j.Neo4jDBConnection;

import org.example.entidades.*;

import org.example.entidades.Amenity;
import org.example.entidades.Hotel;
import org.example.entidades.Habitacion;
import org.example.entidades.PuntoDeInteres;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Record;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                                "RETURN DISTINCT a.id_amenity AS id_amenity",
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

    // --------------------------  HABITACIONES DISPONIBLES ----------------------------------------
    public List<Document> getHabitacionesDisponibles(Date checkin, Date checkout) {
        try {
            MongoCollection<Document> reservasCollection = mongoDB.getCollection("reservas");
            MongoCollection<Document> habitacionesCollection = mongoDB.getCollection("habitaciones");

            // Paso 1: Encontrar habitaciones ocupadas en el rango de fechas, excluyendo reservas canceladas
            List<Integer> habitacionesOcupadas = reservasCollection.find(
                            Filters.and(
                                    Filters.ne("estado_reserva", "CANCELADO"), // Excluir reservas canceladas
                                    Filters.lt("checkin", checkout), // Check-in antes del checkout solicitado
                                    Filters.gt("checkout", checkin) // Checkout después del check-in solicitado
                            )
                    )
                    .projection(Projections.include("id_habitacion")) // Proyección solo del id_habitacion
                    .into(new ArrayList<>())
                    .stream()
                    .map(reserva -> reserva.getInteger("id_habitacion")) // Cambia a `getInteger` para `id_habitacion`
                    .collect(Collectors.toList());

            // Paso 2: Si no hay habitaciones ocupadas en el rango, devolver todas las habitaciones
            if (habitacionesOcupadas.isEmpty()) {
                return habitacionesCollection.find().into(new ArrayList<>()); // Retorna todas las habitaciones
            }

            // Paso 3: Si hay habitaciones ocupadas, obtener las habitaciones que no están en el conjunto de habitaciones ocupadas
            List<Document> habitacionesDisponibles = habitacionesCollection.find(
                    Filters.nin("id_habitacion", habitacionesOcupadas) // Filtra por `id_habitacion` en lugar de `_id`
            ).into(new ArrayList<>());

            return habitacionesDisponibles;
        } catch (Exception e) {
            System.err.println("Error al consultar habitaciones disponibles: " + e.getMessage());
            return null;
        }
    }

// --------------------------  RESERVAS POR FECHA  ----------------------------------------

    public List<Document> ReservasPorFechaYHotel(Date fechaDada, int hotelId) {
        List<Document> reservas = new ArrayList<>();

        try {
            MongoCollection<Document> reservasCollection = mongoDB.getCollection("reservas");

            // Ejecuta la consulta en MongoDB para obtener reservas en el rango de fechas y para el hotel específico
            reservas = reservasCollection.find(
                    Filters.and(
                            Filters.lte("checkin", fechaDada),
                            Filters.gte("checkout", fechaDada),
                            Filters.eq("id_hotel", hotelId) // Filtro adicional para el ID del hotel
                    )
            ).into(new ArrayList<>());

        } catch (Exception e) {
            System.err.println("Error al consultar reservas por fecha y hotel: " + e.getMessage());
        }

        return reservas;
    }


// --------------------------  DETALLE DE HUESPED POR ID ----------------------------------------

    public Document HuespedPorID(int idHuesped) {
        try {
            MongoCollection<Document> huespedesCollection = mongoDB.getCollection("huespedes");

            // Ejecuta la consulta en MongoDB para encontrar el huésped por ID
            Document huesped = huespedesCollection.find(
                    new Document("id_huesped", idHuesped)
            ).first();

            return huesped;

        } catch (Exception e) {
            System.err.println("Error al consultar el huésped por ID: " + e.getMessage());
            return null;
        }
    }



    public List<Hotel> getHotelesCercanosAPOI(int idPoi) {
        List<Hotel> hoteles = new ArrayList<>();
        try (Session session = neo4jDB.session()) {
            Result result = session.run(
                "MATCH (h:hotel)-[:PERTENECE]->(z:zona)<-[:PERTENECE]-(p:poi {id_poi: $idPoi}) " +
                "RETURN h.id_hotel AS id_hotel",
                Map.of("idPoi", idPoi)
            );
            List<Integer> hotelIds = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                hotelIds.add(record.get("id_hotel").asInt());
            }
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
                        (Map<String, String>) doc.get("direccion"),
                        (List<Integer>) doc.get("habitaciones"),
                        doc.getInteger("zona")
                    );
                    hoteles.add(hotel);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener hoteles cercanos a un punto de interés: " + e.getMessage());
        }
        return hoteles;
    }

    public List<PuntoDeInteres> getAllPuntosDeInteres() {
        List<PuntoDeInteres> puntosDeInteres = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("pois");
            for (Document doc : collection.find()) {
                PuntoDeInteres poi = new PuntoDeInteres(
                    doc.getObjectId("_id"),
                    doc.getInteger("id_poi"),
                    doc.getString("nombre"),
                    doc.getString("descripcion"),
                    doc.getInteger("zona")
                );
                puntosDeInteres.add(poi);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener todos los puntos de interés: " + e.getMessage());
        }
        return puntosDeInteres;
    }

    public List<Hotel> getHotelesDisponibles() {
        List<Hotel> hoteles = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("hoteles");
            for (Document doc : collection.find()) {
                Hotel hotel = new Hotel(
                    doc.getObjectId("_id"),
                    doc.getInteger("id_hotel"),
                    doc.getString("nombre"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    (Map<String, String>) doc.get("direccion"),
                    (List<Integer>) doc.get("habitaciones"),
                    doc.getInteger("zona")
                );
                hoteles.add(hotel);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los hoteles disponibles: " + e.getMessage());
        }
        return hoteles;
    }

    public List<Habitacion> getHabitacionesDisponibles() {
        List<Habitacion> habitaciones = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");
            for (Document doc : collection.find()) {
                Habitacion habitacion = new Habitacion(
                    doc.getInteger("id_habitacion"),
                    doc.getInteger("nro_habitacion"),
                    doc.getInteger("id_hotel"),
                    doc.getString("tipo_habitacion"),
                    doc.getList("amenities", Integer.class)
                );
                habitaciones.add(habitacion);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener las habitaciones disponibles: " + e.getMessage());
        }
        return habitaciones;
    }

    public int getAmenityIdByName(String nombre) {
        // Acceder a la colección de amenities en MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");

        // Buscar el documento que tenga el nombre especificado
        Document doc = collection.find(Filters.eq("nombre", nombre)).first();

        // Verificar si el documento fue encontrado
        if (doc != null) {
            return doc.getInteger("id_amenity"); // Devuelve el ID del amenity
        } else {
            System.err.println("Amenity con nombre '" + nombre + "' no encontrado.");
            return -1; // Indica que el amenity no fue encontrado
        }
    }


}
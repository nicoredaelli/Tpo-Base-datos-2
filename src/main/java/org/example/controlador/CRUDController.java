package org.example.controlador;


import com.mongodb.ReadPreference;
import com.mongodb.client.FindIterable;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.conexionmongo.MongoDBConnection;
import org.example.conexionneo4j.Neo4jDBConnection;
import org.example.entidades.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CRUDController {

    private final MongoDatabase mongoDB;
    private final Driver neo4jDB;

    public CRUDController() {
        this.mongoDB = MongoDBConnection.getDatabase();
        this.neo4jDB = Neo4jDBConnection.getConnection();
    }

    // CRUD para la entidad Hotel
    public void createHotel(Hotel hotel) {
        try {
            // MongoDB
            MongoCollection<Document> collection = mongoDB.getCollection("hoteles");
            Document document = new Document("_id", hotel.getObjectIDHotel())
                    .append("id_hotel", hotel.getIdHotel())
                    .append("nombre", hotel.getNombre())
                    .append("telefono", hotel.getTelefono())
                    .append("email", hotel.getEmail())
                    .append("direccion", hotel.getDireccion())
                    .append("habitaciones", hotel.getHabitaciones())
                    .append("zona", hotel.getZona());
            collection.insertOne(document);
            this.aumentarUltimoIdHotel();
        } catch (Exception e) {
            System.err.println("Error al insertar un hotel en MongoDB: " + e.getMessage());
        }
    
        // Neo4j
        try (Session session = neo4jDB.session()) {
            Zona zonaHotel = this.readZona(hotel.getZona());
            if (zonaHotel != null) {
                session.writeTransaction(tx -> {
                    tx.run(
                        "MERGE (h:hotel {id_hotel: $id_hotel, nombre: $nombre_hotel}) " +
                                "MERGE (z:zona {id_zona: $id_zona, nombre: $nombre_zona}) " +
                                "MERGE (h)-[:PERTENECE]->(z)",
                        Map.of(
                                "id_hotel", hotel.getIdHotel(),
                                "nombre_hotel", hotel.getNombre(),
                                "id_zona", zonaHotel.getIdZona(),
                                "nombre_zona", zonaHotel.getNombre()
                        )
                    );
                    return null;
                });
            } else {
                System.err.println("Zona no encontrada");
            }
        } catch (Exception e) {
            System.err.println("Error al insertar hotel en Neo4j: " + e.getMessage());
        }
    }

    public Hotel readHotel(int idHotel) {
        try {
            // Obtener la colección de hoteles en MongoDB
            MongoCollection<Document> collection = mongoDB.getCollection("hoteles");

            // Buscar el documento del hotel según el `id_hotel`
            Document doc = collection.find(Filters.eq("id_hotel", idHotel)).first();

            // Si el documento existe, mapearlo a la clase `Hotel`
            if (doc != null) {
                // Obtener `direccion` como un `Document` y convertirlo a un `Map`
                Document direccionDoc = doc.get("direccion", Document.class);
                Map<String, String> direccion = direccionDoc != null ? direccionDoc.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()))
                        : null;

                // Obtener `habitaciones` como una lista de enteros
                List<Integer> habitaciones = doc.getList("habitaciones", Integer.class);

                return new Hotel(
                        doc.getObjectId("_id"),
                        doc.getInteger("id_hotel"),
                        doc.getString("nombre"),
                        doc.getString("telefono"),
                        doc.getString("email"),
                        direccion,
                        habitaciones,
                        doc.getInteger("zona")
                );
            } else {
                System.out.println("Hotel con id_hotel " + idHotel + " no encontrado en MongoDB.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al leer el hotel de MongoDB: " + e.getMessage());
            return null;
        }
    }


    public void updateHotel(int idHotel, String nuevoNombre, String nuevoTelefono, Map<String, String> nuevaDireccion, String nuevoEmail, List<Integer> nuevasHabitaciones, int nuevaZona) {

        // Actualizar el hotel en MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("hoteles");

        collection.updateOne(
                Filters.eq("id_hotel", idHotel),
                Updates.combine(
                        Updates.set("nombre", nuevoNombre),
                        Updates.set("telefono", nuevoTelefono),
                        Updates.set("direccion", nuevaDireccion),
                        Updates.set("email", nuevoEmail),
                        Updates.set("habitaciones", nuevasHabitaciones),
                        Updates.set("zona", nuevaZona)
                )
        );

        // Neo4j: Actualizar la relación entre el hotel y la zona, y gestionar las habitaciones
        try (Session session = neo4jDB.session()) {
            // Primero, eliminar la relación existente del hotel con la zona anterior
            session.writeTransaction(tx -> {
                tx.run(
                        "MATCH (h:hotel {id_hotel: $id_hotel})-[r:PERTENECE]->(z:zona) " +
                                "DELETE r",
                        Map.of("id_hotel", idHotel)
                );
                return null;
            });

            // Luego, crear la nueva relación con la nueva zona
            Zona nuevaZonaInfo = this.readZona(nuevaZona);  // Obtenemos la información de la nueva zona desde MongoDB
            if (nuevaZonaInfo != null) {
                session.writeTransaction(tx -> {
                    tx.run(
                            "MERGE (h:hotel {id_hotel: $id_hotel, nombre: $nombre_hotel}) " +
                                    "MERGE (z:zona {id_zona: $id_zona, nombre: $nombre_zona}) " +
                                    "MERGE (h)-[:PERTENECE]->(z)",
                            Map.of(
                                    "id_hotel", idHotel,
                                    "nombre_hotel", nuevoNombre,
                                    "id_zona", nuevaZonaInfo.getIdZona(),
                                    "nombre_zona", nuevaZonaInfo.getNombre()
                            )
                    );
                    return null;
                });
            } else {
                System.err.println("Zona no encontrada en MongoDB");
            }

            // Actualizar la relación entre el hotel y sus habitaciones
            session.writeTransaction(tx -> {
                // Primero, eliminar las relaciones actuales de las habitaciones del hotel en Neo4j
                tx.run(
                        "MATCH (h:hotel {id_hotel: $id_hotel})-[r:TIENE_HABITACION]->(:habitacion) " +
                                "DELETE r",
                        Map.of("id_hotel", idHotel)
                );
                return null;
            });

            // Agregar las nuevas relaciones de habitaciones
            for (Integer idHabitacion : nuevasHabitaciones) {
                Habitacion habitacionInfo = this.readHabitacion(idHabitacion);  // Obtener información completa de cada habitación
                if (habitacionInfo != null) {
                    session.writeTransaction(tx -> {
                        tx.run(
                                "MERGE (h:hotel {id_hotel: $id_hotel}) " +
                                        "MERGE (hab:habitacion {id_habitacion: $id_habitacion, nro_habitacion: $nro_habitacion, tipo_habitacion: $tipo_habitacion}) " +
                                        "MERGE (h)-[:TIENE_HABITACION]->(hab)",
                                Map.of(
                                        "id_hotel", idHotel,
                                        "id_habitacion", habitacionInfo.getIdHabitacion(),
                                        "nro_habitacion", habitacionInfo.getNroHabitacion(),
                                        "tipo_habitacion", habitacionInfo.getTipoHabitacion()
                                )
                        );
                        return null;
                    });

                    // Crear o actualizar las relaciones entre la habitación y sus amenities
                    session.writeTransaction(tx -> {
                        tx.run(
                                "MATCH (hab:habitacion {id_habitacion: $id_habitacion})-[r:TIENE_AMENITY]->(:amenity) " +
                                        "DELETE r",
                                Map.of("id_habitacion", habitacionInfo.getIdHabitacion())
                        );
                        return null;
                    });

                    for (Integer idAmenity : habitacionInfo.getAmenities()) {
                        Amenity amenityInfo = this.readAmenity(idAmenity);
                        if (amenityInfo != null) {
                            session.writeTransaction(tx -> {
                                tx.run(
                                        "MERGE (hab:habitacion {id_habitacion: $id_habitacion}) " +
                                                "MERGE (a:amenity {id_amenity: $id_amenity, nombre: $nombre_amenity, descripcion: $descripcion_amenity}) " +
                                                "MERGE (hab)-[:TIENE_AMENITY]->(a)",
                                        Map.of(
                                                "id_habitacion", habitacionInfo.getIdHabitacion(),
                                                "id_amenity", amenityInfo.getIdAmenity(),
                                                "nombre_amenity", amenityInfo.getNombre(),
                                                "descripcion_amenity", amenityInfo.getDescripcion()
                                        )
                                );
                                return null;
                            });
                        } else {
                            System.err.println("Amenity con ID " + idAmenity + " no encontrado en MongoDB.");
                        }
                    }
                } else {
                    System.err.println("Habitación con ID " + idHabitacion + " no encontrada en MongoDB.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar el hotel en Neo4j: " + e.getMessage());
        }
    }


    public void deleteHotel(int idHotel) {

        // Eliminar el hotel de la base de datos MongoDB

        Hotel hotelAEliminar = this.readHotel(idHotel);

        MongoCollection<Document> collection = mongoDB.getCollection("hoteles");
        collection.deleteOne(Filters.eq("id_hotel", hotelAEliminar.getIdHotel()));


        // Eliminar el hotel de base de datos Neo4j

        try (Session session = neo4jDB.session()) {
            session.writeTransaction(tx -> {
                tx.run(
                        "MATCH (h:hotel {id_hotel: $idHotel})-[r:PERTENECE]->(z:zona {id_zona: $idZona}) " +
                                "DETACH DELETE h",
                        Map.of("idHotel", hotelAEliminar.getIdHotel(), "idZona", hotelAEliminar.getZona())
                );
                return null;
            });
            System.out.println("Hotel eliminado de Neo4j con nombre: " + hotelAEliminar.getNombre());
        } catch (Exception e) {
            System.err.println("Error al eliminar el hotel en Neo4j: " + e.getMessage());
        }
    }
//---------------------------------------------------------------------------------------------------------------------------------
    // CRUD para la entidad PuntoDeInteres
    public void createPuntoDeInteres(PuntoDeInteres poi) {
        // MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("pois");
        Document document = new Document("_id", poi.getObjectIDPoi())
                .append("id_poi", poi.getIdPoi())
                .append("nombre", poi.getNombre())
                .append("descripcion", poi.getDescripcion())
                .append("zona", poi.getZona());
        collection.insertOne(document);
        this.aumentarUltimoIdPuntoDeInteres();

        // Neo4j
        try (Session session = neo4jDB.session()) {
            Zona zonaPoi = this.readZona(poi.getZona());
            if (zonaPoi != null) {
                session.writeTransaction(tx -> {
                    tx.run(
                            "MERGE (p:poi {id_poi: $id_poi, nombre: $nombre_poi}) " +
                                    "MERGE (z:zona {id_zona: $id_zona, nombre: $nombre_zona}) " +
                                    "MERGE (p)-[:PERTENECE]->(z)",
                            Map.of(
                                    "id_poi", poi.getIdPoi(),
                                    "nombre_poi", poi.getNombre(),
                                    "id_zona", zonaPoi.getIdZona(),
                                    "nombre_zona", zonaPoi.getNombre()
                            )
                    );
                    return null;
                });
            } else {
                System.err.println("Zona no encontrada");
            }
        } catch (Exception e) {
            System.err.println("Error al insertar el punto de interes en Neo4j: " + e.getMessage());
        }

    }

    public PuntoDeInteres readPuntoDeInteres(int idPoi) {
        MongoCollection<Document> collection = mongoDB.getCollection("pois");
        Document doc = collection.find(Filters.eq("id_poi", idPoi)).first();
        return (doc != null) ? new PuntoDeInteres(
                doc.getObjectId("_id"),
                doc.getInteger("id_poi"),
                doc.getString("nombre"),
                doc.getString("descripcion"),
                doc.getInteger("zona")
        ) : null;
    }

    public void updatePuntoDeInteres(int idPoi, String nuevoNombre, String nuevaDescripcion, int nuevaZona) {
        MongoCollection<Document> collection = mongoDB.getCollection("pois");

        // actualizar el punto de interes en MongoDB

        collection.updateOne(
                Filters.eq("id_poi", idPoi),
                Updates.combine(
                        Updates.set("nombre", nuevoNombre),
                        Updates.set("descripcion", nuevaDescripcion),
                        Updates.set("zona", nuevaZona)
                )
        );

        // Neo4j: Actualizar la relación entre la zona y el punto de interes
        try (Session session = neo4jDB.session()) {
            // Primero, elimina la relación existente del punto de interes con la zona anterior
            session.writeTransaction(tx -> {
                tx.run(
                        "MATCH (p:poi {id_poi: $id_poi})-[r:PERTENECE]->(z:zona) " +
                                "DELETE r",
                        Map.of("id_poi", idPoi)
                );
                return null;
            });

            // Luego, crea la nueva relación con la nueva zona
            Zona nuevaZonaInfo = this.readZona(nuevaZona);  // Obtenemos la información de la nueva zona desde MongoDB
            if (nuevaZonaInfo != null) {
                session.writeTransaction(tx -> {
                    tx.run(
                            "MERGE (p:poi {id_poi: $id_poi, nombre: $nombre_poi}) " +
                                    "MERGE (z:zona {id_zona: $id_zona, nombre: $nombre_zona}) " +
                                    "MERGE (p)-[:PERTENECE]->(z)",
                            Map.of(
                                    "id_poi", idPoi,
                                    "nombre_poi", nuevoNombre,
                                    "id_zona", nuevaZonaInfo.getIdZona(),
                                    "nombre_zona", nuevaZonaInfo.getNombre()
                            )
                    );
                    return null;
                });
            } else {
                System.err.println("Zona no encontrada en MongoDB");
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar el punto de interes en Neo4j: " + e.getMessage());
        }
    }

    public void deletePuntoDeInteres(int idPoi) {
        // Eliminar el punto de interes de la base de datos MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("pois");
        PuntoDeInteres puntoDeInteresAEliminar = this.readPuntoDeInteres(idPoi);

        collection.deleteOne(Filters.eq("id_poi", puntoDeInteresAEliminar.getIdPoi()));

        // Eliminar el punto de interes de base de datos Neo4j

        try (Session session = neo4jDB.session()) {
            session.writeTransaction(tx -> {
                tx.run(
                        "MATCH (p:poi {id_poi: $idPoi})-[r:PERTENECE]->(z:zona {id_zona: $idZona}) " +
                                "DETACH DELETE p",
                        Map.of("idPoi", puntoDeInteresAEliminar.getIdPoi(), "idZona", puntoDeInteresAEliminar.getZona())
                );
                return null;
            });
            System.out.println("Punto de interes eliminado de Neo4j con nombre: " + puntoDeInteresAEliminar.getNombre());
        } catch (Exception e) {
            System.err.println("Error al eliminar el punto de interes en Neo4j: " + e.getMessage());
        }
    }


    public List<PuntoDeInteres> getPuntosDeInteresDisponibles() {
        List<PuntoDeInteres> puntosDeInteres = new ArrayList<>();
    
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("pois");
    
            // Comprobar si la colección tiene documentos
            if (collection.countDocuments() == 0) {
                System.out.println("La colección de puntos de interés está vacía.");
            } else {
                System.out.println("Documentos encontrados en la colección de puntos de interés: " + collection.countDocuments());
            }
    
            // Obtener todos los documentos y convertirlos a objetos PuntoDeInteres
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
            System.err.println("Error al obtener los puntos de interés: " + e.getMessage());
        }
    
        return puntosDeInteres;
    }
    
    
//--------------------------------------------------------------------------------------------------------------------------------
// CRUD para la entidad Amenity
public void createAmenity(Amenity amenity) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");
        Document document = new Document("_id", amenity.getObjectIdAmenitie())
                .append("id_amenity", amenity.getIdAmenity())
                .append("nombre", amenity.getNombre())
                .append("descripcion", amenity.getDescripcion());
        collection.insertOne(document);
        this.aumentarUltimoIdAmenity();
        System.out.println("Amenity insertado en MongoDB: " + amenity.getNombre());
    } catch (Exception e) {
        System.err.println("Error al insertar amenity en MongoDB: " + e.getMessage());
    }

    // Neo4j
    try (Session session = neo4jDB.session()) {
        session.writeTransaction(tx -> {
            tx.run("MERGE (a:amenity {id_amenity: $idAmenity, nombre: $nombre, descripcion: $descripcion})",
                    Map.of(
                            "idAmenity", amenity.getIdAmenity(),
                            "nombre", amenity.getNombre(),
                            "descripcion", amenity.getDescripcion()
                    ));
            return null;
        });
        System.out.println("Amenity insertado en Neo4j: " + amenity.getNombre());
    } catch (Exception e) {
        System.err.println("Error al insertar amenity en Neo4j: " + e.getMessage());
    }
}

public Amenity readAmenity(int idAmenity) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");
        Document doc = collection.find(Filters.eq("id_amenity", idAmenity)).first();
        if (doc != null) {
            return new Amenity(
                    doc.getObjectId("_id"),
                    doc.getInteger("id_amenity"),
                    doc.getString("nombre"),
                    doc.getString("descripcion")
            );
        } else {
            //System.out.println("Amenity no encontrado en MongoDB con idAmenity: " + idAmenity);
            return null;
        }
    } catch (Exception e) {
        System.err.println("Error al leer amenity de MongoDB: " + e.getMessage());
        return null;
    }
}

public void updateAmenity(Amenity amenity) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");
        Document updatedData = new Document("nombre", amenity.getNombre())
                .append("descripcion", amenity.getDescripcion());
        
        Document updateOperation = new Document("$set", updatedData);
        
        collection.updateOne(Filters.eq("id_amenity", amenity.getIdAmenity()), updateOperation);
        System.out.println("Amenity actualizado en MongoDB: " + amenity.getNombre());
    } catch (Exception e) {
        System.err.println("Error al actualizar amenity en MongoDB: " + e.getMessage());
    }

    // Neo4j
    try (Session session = neo4jDB.session()) {
        session.writeTransaction(tx -> {
            tx.run("MATCH (a:amenity {id_amenity: $id_amenity}) " +
                            "SET a.nombre = $nombre, a.descripcion = $descripcion",
                    Map.of(
                            "id_amenity", amenity.getIdAmenity(),
                            "nombre", amenity.getNombre(),
                            "descripcion", amenity.getDescripcion()
                    ));
            return null;
        });
        System.out.println("Amenity actualizado en Neo4j: " + amenity.getNombre());
    } catch (Exception e) {
        System.err.println("Error al actualizar amenity en Neo4j: " + e.getMessage());
    }
}


public void deleteAmenity(int idAmenity) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");
        collection.deleteOne(Filters.eq("id_amenity", idAmenity));
        System.out.println("Amenity eliminado en MongoDB con idAmenity: " + idAmenity);
    } catch (Exception e) {
        System.err.println("Error al eliminar amenity en MongoDB: " + e.getMessage());
    }

    // Neo4j
    try (Session session = neo4jDB.session()) {
        session.writeTransaction(tx -> {
            tx.run("MATCH (a:amenity{id_amenity: $id_amenity}) DELETE a",
                    Map.of("id_amenity", idAmenity));
            return null;
        });
        System.out.println("Amenity eliminado en Neo4j con idAmenity: " + idAmenity);
    } catch (Exception e) {
        System.err.println("Error al eliminar amenity en Neo4j: " + e.getMessage());
    }
}
public List<Amenity> getAmenitiesDisponibles() {
    List<Amenity> amenities = new ArrayList<>();

    try {
        // Conectar a la base de datos y obtener la colección de amenities
        MongoCollection<Document> collection = mongoDB.getCollection("amenities");

        // Obtener todos los documentos y convertirlos a objetos Amenity
        for (Document doc : collection.find()) {
            Amenity amenity = new Amenity(
                doc.getObjectId("_id")
                , doc.getInteger("id_amenity"),
                doc.getString("nombre"),
                doc.getString("descripcion")
            );
            amenities.add(amenity);
        }

    } catch (Exception e) {
        System.err.println("Error al obtener los amenities: " + e.getMessage());
    }

    return amenities;
}

//------------------------------------------------------------------------------------------------------------------------------------
    // CRUD para la entidad Habitacion neo4jDB mongoDB
//------------------------------------------------------------------------------------------------------------------------------------
// CRUD para la entidad Habitacion
public void createHabitacion(Habitacion habitacion) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");
        Document document = new Document("_id", new ObjectId())
                .append("id_habitacion", habitacion.getIdHabitacion())
                .append("nro_habitacion", habitacion.getNroHabitacion())
                .append("id_hotel", habitacion.getIdHotel())
                .append("tipo_habitacion", habitacion.getTipoHabitacion())
                .append("amenities", habitacion.getAmenities());
        collection.insertOne(document);
        this.aumentarUltimoIDHabitacion();
        System.out.println("Habitación insertada en MongoDB: " + habitacion.getNroHabitacion());
    } catch (Exception e) {
        System.err.println("Error al insertar habitación en MongoDB: " + e.getMessage());
    }

    //String nuevoNombre, String nuevoTelefono, Map<String, String> nuevaDireccion, String nuevoEmail, int nuevaZona) {


    Hotel hotelParaActualizar = this.readHotel(habitacion.getIdHotel());
    // Crea una nueva lista basada en la lista existente
    List<Integer> habitacionesNuevas = new ArrayList<>(hotelParaActualizar.getHabitaciones());

    // Agrega la nueva habitación a la lista
    habitacionesNuevas.add(habitacion.getIdHabitacion());

    this.updateHotel(
            hotelParaActualizar.getIdHotel(),
            hotelParaActualizar.getNombre(),
            hotelParaActualizar.getTelefono(),
            hotelParaActualizar.getDireccion(),
            hotelParaActualizar.getEmail(),
            habitacionesNuevas,
            hotelParaActualizar.getZona()
    );

    // Neo4j
    try (Session session = neo4jDB.session()) {
        session.writeTransaction(tx -> {
            tx.run("MERGE (h:habitacion {id_habitacion: $idHabitacion}) " +
                            "ON CREATE SET h.nro_habitacion = $nroHabitacion, h.tipo_habitacion = $tipoHabitacion " +
                            "ON MATCH SET h.nro_habitacion = $nroHabitacion, h.tipo_habitacion = $tipoHabitacion " +
                            "WITH h " +
                            "MERGE (hotel:hotel {id_hotel: $idHotel}) " +
                            "MERGE (hotel)-[:TIENE_HABITACION]->(h) " +
                            "WITH h " +
                            "UNWIND $amenities AS amenityId " +
                            "MERGE (a:amenity {id_amenity: amenityId}) " +
                            "MERGE (h)-[:TIENE_AMENITY]->(a)",
                    Map.of(
                            "idHabitacion", habitacion.getIdHabitacion(),
                            "nroHabitacion", habitacion.getNroHabitacion(),
                            "tipoHabitacion", habitacion.getTipoHabitacion(),
                            "idHotel", habitacion.getIdHotel(),
                            "amenities", habitacion.getAmenities()
                    ));
            return null;
        });
        System.out.println("Habitación insertada en Neo4j: " + habitacion.getNroHabitacion());
    } catch (Exception e) {
        System.err.println("Error al insertar habitación en Neo4j: " + e.getMessage());
    }
}


    public Habitacion readHabitacion(int idHabitacion) {
        // MongoDB
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");
            Document doc = collection.find(Filters.eq("id_habitacion", idHabitacion)).first();

            if (doc != null) {
                // Obtener solo los IDs de los amenities
                List<Integer> amenities = doc.getList("amenities", Integer.class);

                // Crear y retornar la instancia de Habitacion con solo los IDs de amenities
                return new Habitacion(
                        doc.getInteger("id_habitacion"),
                        doc.getInteger("nro_habitacion"),
                        doc.getInteger("id_hotel"),
                        doc.getString("tipo_habitacion"),
                        amenities // Lista de IDs de amenities
                );
            } else {
                System.out.println("Habitación no encontrada en MongoDB con id habitacion: " + idHabitacion);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al leer habitación de MongoDB: " + e.getMessage());
            return null;
        }
    }

    public void updateHabitacion(Habitacion habitacion) {
        // MongoDB
        try {
            MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");
            Document updatedData = new Document("id_hotel", habitacion.getIdHotel())
                    .append("nro_habitacion", habitacion.getNroHabitacion())
                    .append("tipo_habitacion", habitacion.getTipoHabitacion())
                    .append("amenities", habitacion.getAmenities());

            Document updateOperation = new Document("$set", updatedData);
            collection.updateOne(Filters.eq("id_habitacion", habitacion.getIdHabitacion()), updateOperation);
            System.out.println("Habitación actualizada en MongoDB: " + habitacion.getIdHabitacion());

            // Actualizar la lista de habitaciones del hotel si el ID de hotel ha cambiado
            Hotel hotelAnterior = this.readHotel(habitacion.getIdHotel());
            List<Integer> habitacionesActualizadas = new ArrayList<>(hotelAnterior.getHabitaciones());
            habitacionesActualizadas.add(habitacion.getIdHabitacion());

            this.updateHotel(
                    hotelAnterior.getIdHotel(),
                    hotelAnterior.getNombre(),
                    hotelAnterior.getTelefono(),
                    hotelAnterior.getDireccion(),
                    hotelAnterior.getEmail(),
                    habitacionesActualizadas,
                    hotelAnterior.getZona()
            );
        } catch (Exception e) {
            System.err.println("Error al actualizar habitación en MongoDB: " + e.getMessage());
        }

        // Neo4j
        try (Session session = neo4jDB.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (h:habitacion {id_habitacion: $idHabitacion}) " +
                                "SET h.nro_habitacion = $nroHabitacion, h.tipo_habitacion = $tipoHabitacion " +
                                "WITH h " +
                                "MATCH (hotel:hotel {id_hotel: $idHotel}) " +
                                "MERGE (hotel)-[:TIENE_HABITACION]->(h) " +
                                "WITH h " +
                                "OPTIONAL MATCH (h)-[r:TIENE_AMENITY]->() DELETE r " +
                                "WITH h " +
                                "UNWIND $amenities AS amenityId " +
                                "MERGE (a:amenity {id_amenity: amenityId}) " +
                                "MERGE (h)-[:TIENE_AMENITY]->(a)",
                        Map.of(
                                "idHabitacion", habitacion.getIdHabitacion(),
                                "nroHabitacion", habitacion.getNroHabitacion(),
                                "tipoHabitacion", habitacion.getTipoHabitacion(),
                                "idHotel", habitacion.getIdHotel(),
                                "amenities", habitacion.getAmenities()
                        ));
                return null;
            });
            System.out.println("Habitación actualizada en Neo4j: " + habitacion.getIdHabitacion());
        } catch (Exception e) {
            System.err.println("Error al actualizar habitación en Neo4j: " + e.getMessage());
        }
    }


    public void deleteHabitacion(int idHabitacion) {
        Habitacion habitacion = this.readHabitacion(idHabitacion);
        if (habitacion != null) {
            int idHotel = habitacion.getIdHotel();

            // MongoDB
            try {
                MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");
                collection.deleteOne(Filters.eq("id_habitacion", idHabitacion));
                System.out.println("Habitación eliminada en MongoDB con idHabitacion: " + idHabitacion);

                // Actualizar la lista de habitaciones del hotel eliminando la habitación
                Hotel hotelParaActualizar = this.readHotel(idHotel);
                List<Integer> habitacionesActualizadas = new ArrayList<>(hotelParaActualizar.getHabitaciones());
                habitacionesActualizadas.remove(Integer.valueOf(idHabitacion));  // Remover el ID de la habitación

                this.updateHotel(
                        hotelParaActualizar.getIdHotel(),
                        hotelParaActualizar.getNombre(),
                        hotelParaActualizar.getTelefono(),
                        hotelParaActualizar.getDireccion(),
                        hotelParaActualizar.getEmail(),
                        habitacionesActualizadas,
                        hotelParaActualizar.getZona()
                );
            } catch (Exception e) {
                System.err.println("Error al eliminar habitación en MongoDB: " + e.getMessage());
            }

            // Neo4j
            try (Session session = neo4jDB.session()) {
                session.writeTransaction(tx -> {
                    tx.run("MATCH (h:habitacion {id_habitacion: $idHabitacion}) " +
                                    "DETACH DELETE h",
                            Map.of("idHabitacion", idHabitacion));
                    return null;
                });
                System.out.println("Habitación eliminada en Neo4j con idHabitacion: " + idHabitacion);
            } catch (Exception e) {
                System.err.println("Error al eliminar habitación en Neo4j: " + e.getMessage());
            }
        } else {
            System.out.println("Habitación no encontrada para eliminar.");
        }
    }


    public List<Hotel> getHotelesDisponibles() {
        List<Hotel> hoteles = new ArrayList<>();

        try {
            // Conectar a la base de datos y obtener la colección de hoteles
            MongoCollection<Document> collection = mongoDB.getCollection("hoteles");

            // Obtener todos los documentos y convertirlos a objetos Hotel
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
            System.err.println("Error al obtener los hoteles: " + e.getMessage());
        }

        return hoteles;
    }


    public List<Habitacion> getHabitacionesDisponibles() {
        List<Habitacion> habitaciones = new ArrayList<>();

        // Conectar a la colección de habitaciones
        MongoCollection<Document> collection = mongoDB.getCollection("habitaciones");

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int idHabitacion = doc.getInteger("idHabitacion"); // Obtener el ID de la habitación
                int nroHabitacion = doc.getInteger("nroHabitacion");
                int idHotel = doc.getInteger("idHotel");
                String tipoHabitacion = doc.getString("tipoHabitacion");
                List<Integer> amenities = (List<Integer>) doc.get("amenities");

                // Crear un objeto Habitacion y agregarlo a la lista
                Habitacion habitacion = new Habitacion(idHabitacion, nroHabitacion, idHotel, tipoHabitacion, amenities);
                habitaciones.add(habitacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores, puedes lanzar una excepción o retornar una lista vacía
        }

        return habitaciones;
    }


    //------------------------------------------------------------------------------------------------------------------------------------
    // CRUD para la entidad Reserva
public void createReserva(Reserva reserva) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("reservas");
        Document document = new Document("_id", new ObjectId())
                .append("cod_reserva", reserva.getCodReserva())
                .append("checkin", reserva.getCheckin())
                .append("checkout", reserva.getCheckout())
                .append("estado_reserva", reserva.getEstadoReserva())
                .append("tarifa", reserva.getTarifa())
                .append("id_hotel", reserva.getIdHotel())
                .append("id_habitacion", reserva.getIdHabitacion())
                .append("id_huesped", reserva.getIdHuesped());
        collection.insertOne(document);
        this.aumentarUltimoCodReserva();
        System.out.println("Reserva insertada en MongoDB: " + reserva.getCodReserva());
    } catch (Exception e) {
        System.err.println("Error al insertar reserva en MongoDB: " + e.getMessage());
    }
}

public Reserva readReserva(int codReserva) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("reservas");
        Document doc = collection.find(Filters.eq("cod_reserva", codReserva)).first();
        if (doc != null) {
            return new Reserva(
                    doc.getInteger("cod_reserva"),
                    doc.getDate("checkin"),
                    doc.getDate("checkout"),
                    EstadoReserva.valueOf(doc.getString("estado_reserva")),
                    doc.getDouble("tarifa"),
                    doc.getInteger("id_hotel"),
                    doc.getInteger("id_habitacion"),
                    doc.getInteger("id_huesped")
            );
        } else {
            System.out.println("Reserva no encontrada en MongoDB con codReserva: " + codReserva);
            return null;
        }
    } catch (Exception e) {
        System.err.println("Error al leer reserva de MongoDB: " + e.getMessage());
        return null;
    }
}

public void updateReserva(Reserva reserva) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("reservas");
        Document updatedData = new Document("checkin", reserva.getCheckin())
                .append("checkout", reserva.getCheckout())
                .append("estado_reserva", reserva.getEstadoReserva())
                .append("tarifa", reserva.getTarifa())
                .append("id_hotel", reserva.getIdHotel())
                .append("id_habitacion", reserva.getIdHabitacion())
                .append("id_huesped", reserva.getIdHuesped());
        
        Document updateOperation = new Document("$set", updatedData);
        
        collection.updateOne(Filters.eq("cod_reserva", reserva.getCodReserva()), updateOperation);
        System.out.println("Reserva actualizada en MongoDB: " + reserva.getCodReserva());
    } catch (Exception e) {
        System.err.println("Error al actualizar reserva en MongoDB: " + e.getMessage());
    }
}

public void deleteReserva(int codReserva) {
    // MongoDB
    try {
        MongoCollection<Document> collection = mongoDB.getCollection("reservas");
        collection.deleteOne(Filters.eq("cod_reserva", codReserva));
        System.out.println("Reserva eliminada en MongoDB con codReserva: " + codReserva);
    } catch (Exception e) {
        System.err.println("Error al eliminar reserva en MongoDB: " + e.getMessage());
    }
}

public List<Reserva> getReservasDisponibles() {
        List<Reserva> reservas = new ArrayList<>();
        MongoCollection<Document> collection = mongoDB.getCollection("reservas"); // Cambia el nombre de la colección según tu estructura

        // Obtener todas las reservas de la colección
        FindIterable<Document> documentos = collection.find();
        for (Document doc : documentos) {
            int codReserva = doc.getInteger("codReserva");
            Date checkin = (Date) doc.getDate("checkin");
            Date checkout = (Date) doc.getDate("checkout");
            EstadoReserva estadoReserva = EstadoReserva.valueOf(doc.getString("estadoReserva")); // Asegúrate de que coincide con tu Enum
            double tarifa = doc.getDouble("tarifa");
            int idHotel = doc.getInteger("idHotel");
            int idHabitacion = doc.getInteger("idHabitacion");
            int idHuesped = doc.getInteger("idHuesped");

            // Crear objeto Reserva y agregarlo a la lista
            Reserva reserva = new Reserva(codReserva, checkin, checkout, estadoReserva, tarifa, idHotel, idHabitacion, idHuesped);
            reservas.add(reserva);
        }
        return reservas;
    }
   


    

//------------------------------------------------------------------------------------------------------------------------------------
    // CRUD para la entidad Huesped

     // Create Huesped
    public void createHuesped(Huesped huesped) {
        try {
            // MongoDB: Insertar huesped
            MongoCollection<Document> collection = mongoDB.getCollection("huespedes");
            Document doc = new Document("_id", huesped.getObjectIdHuesped())
                    .append("id_huesped", huesped.getIdHuesped())
                    .append("nombre", huesped.getNombre())
                    .append("apellido", huesped.getApellido())
                    .append("telefono", huesped.getTelefono())
                    .append("email", huesped.getEmail())
                    .append("direccion", huesped.getDireccion());
            collection.insertOne(doc);
            this.aumentarUltimoIdHuesped();
            System.out.println("Huesped insertado en MongoDB.");

            /*
            // Neo4j: Crear nodo de huesped
            try (Session session = neo4jDB.session()) {
                try (Transaction tx = session.beginTransaction()) {
                tx.run("CREATE (h:Huesped {id: $id, nombre: $nombre, apellido: $apellido, telefono: $telefono, email: $email, direccion: $direccion})",
                        Map.of(
                                "id", huesped.getIdHuesped().toHexString(),
                                "nombre", huesped.getNombre(),
                                "apellido", huesped.getApellido(),
                                "telefono", huesped.getTelefono(),
                                "email", huesped.getEmail(),
                                "direccion", huesped.getDireccion().toString()
                        ));
                tx.commit();
                System.out.println("Huesped insertado en Neo4j.");
                }
            } */
        } catch (Exception e) {
            System.err.println("Error al crear Huesped: " + e.getMessage());
        }
    }

    // Read Huesped
    public Huesped readHuesped(int idHuesped) {
        Huesped huesped = null;
        try {
            // MongoDB: Buscar huesped
            MongoCollection<Document> collection = mongoDB.getCollection("huespedes");
            Document doc = collection.find(new Document("id_huesped", idHuesped)).first();
            if (doc != null) {
                // Obtener la dirección como un Document y convertirlo a Map
                Document direccionDoc = doc.get("direccion", Document.class);
                Map<String, String> direccion = direccionDoc != null ? direccionDoc.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()))
                        : null;

                huesped = new Huesped(
                        doc.getObjectId("_id"),
                        doc.getInteger("id_huesped"),
                        doc.getString("nombre"),
                        doc.getString("apellido"),
                        doc.getString("telefono"),
                        doc.getString("email"),
                        direccion
                );
            }
        } catch (Exception e) {
            System.err.println("Error al leer Huesped: " + e.getMessage());
        }
        return huesped;
    }

    // Update Huesped
    public void updateHuesped(Huesped huesped) {
        try {
            // MongoDB: Actualizar huesped
            MongoCollection<Document> collection = mongoDB.getCollection("huespedes");
            Document updateDoc = new Document("nombre", huesped.getNombre())
                    .append("apellido", huesped.getApellido())
                    .append("telefono", huesped.getTelefono())
                    .append("email", huesped.getEmail())
                    .append("direccion", huesped.getDireccion());
            collection.updateOne(new Document("id_huesped", huesped.getIdHuesped()), new Document("$set", updateDoc));
            System.out.println("Huesped actualizado en MongoDB.");

            /*
            // Neo4j: Actualizar nodo de huesped
            try (Session session = neo4jDB.session()) {
                try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (h:Huesped {id: $id}) SET h.nombre = $nombre, h.apellido = $apellido, h.telefono = $telefono, h.email = $email, h.direccion = $direccion",
                        Map.of(
                                "id", huesped.getIdHuesped().toHexString(),
                                "nombre", huesped.getNombre(),
                                "apellido", huesped.getApellido(),
                                "telefono", huesped.getTelefono(),
                                "email", huesped.getEmail(),
                                "direccion", huesped.getDireccion().toString()
                        ));
                tx.commit();
                System.out.println("Huesped actualizado en Neo4j.");
            }
            }*/
        } catch (Exception e) {
            System.err.println("Error al actualizar Huesped: " + e.getMessage());
        }
    }

    // Delete Huesped
    public void deleteHuesped(int idHuesped) {
        try {
            // MongoDB: Eliminar huesped
            MongoCollection<Document> collection = mongoDB.getCollection("huespedes");
            collection.deleteOne(new Document("id_huesped", idHuesped));
            System.out.println("Huesped eliminado de MongoDB.");
            /*
            // Neo4j: Eliminar nodo de huesped
            try (Session session = neo4jDB.session()) {
                try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (h:Huesped {id: $id}) DELETE h",
                        Map.of("id", idHuesped.toHexString()));
                tx.commit();
                System.out.println("Huesped eliminado de Neo4j.");
                }   
            }*/
        } catch (Exception e) {
            System.err.println("Error al eliminar Huesped: " + e.getMessage());
        }
    }


    public List<Huesped> getHuespedesDisponibles() {
        List<Huesped> huespedes = new ArrayList<>();
    
        try {
            // Conectar a la base de datos y obtener la colección de huéspedes
            MongoCollection<Document> collection = mongoDB.getCollection("huespedes");
    
            // Obtener todos los documentos y convertirlos a objetos Huesped
            for (Document doc : collection.find()) {
                Map<String, String> direccion = new HashMap<>();
    
                // Acceder al objeto 'direccion' y obtener los campos
                Document direccionDoc = doc.get("direccion", Document.class);
                if (direccionDoc != null) {
                    direccion.put("calle", direccionDoc.getString("calle"));
                    direccion.put("numero", direccionDoc.getString("numero"));
                    direccion.put("provincia", direccionDoc.getString("provincia"));
                    direccion.put("pais", direccionDoc.getString("pais"));
                }

    
                // Crear el objeto Huesped
                Huesped huesped = new Huesped(
                    doc.getObjectId("_id"), // Suponiendo que necesitas el ObjectId
                    doc.getInteger("id_huesped"),
                    doc.getString("nombre"),
                    doc.getString("apellido"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    direccion // Pasar el mapa de dirección
                );
    
                huespedes.add(huesped);
            }
    
        } catch (Exception e) {
            System.err.println("Error al obtener los huéspedes: " + e.getMessage());
        }
    
        return huespedes;
    }
    
    
    
    public Huesped readHuespedByName(String nombre, String apellido) {
        List<Huesped> huespedes = getHuespedesDisponibles(); // Obtener la lista de huéspedes
    
        for (Huesped huesped : huespedes) {
            if (huesped.getNombre().equals(nombre) && huesped.getApellido().equals(apellido)) {
                return huesped; // Devolver el huésped que coincide
            }
        }
        return null; // Si no se encontró, devolver null
    }
    

//----------------------------------------------------------------------------------------------------

// CRUD para la entidad Zona

    public void createZona(Zona zona) {
        // MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("zonas");
        Document document = new Document("id_zona", zona.getIdZona())
                .append("nombre", zona.getNombre())
                .append("provincia", zona.getProvincia())
                .append("pais", zona.getPais())
                .append("descripcion", zona.getDescripcion());
        collection.insertOne(document);
        this.aumentarUltimoIDZona();

        // Neo4j
        try (Session session = neo4jDB.session()) {
            session.writeTransaction(tx -> {
                tx.run("MERGE (z:zona {id_zona: $idZona}) " +
                                "SET z.nombre = $nombre, " +
                                "z.provincia = $provincia, " +
                                "z.pais = $pais, " +
                                "z.descripcion = $descripcion",
                        Map.of(
                                "idZona", zona.getIdZona(),
                                "nombre", zona.getNombre(),
                                "provincia", zona.getProvincia(),
                                "pais", zona.getPais(),
                                "descripcion", zona.getDescripcion()
                        ));
                return null;
            });
            System.out.println("Zona creada en Neo4j con idZona: " + zona.getIdZona());
        } catch (Exception e) {
            System.err.println("Error al crear zona en Neo4j: " + e.getMessage());
        }
    }

    public Zona readZona(int idZona) {
        // MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("zonas");
        Document doc = collection.find(Filters.eq("id_zona", idZona)).first();
        return (doc != null) ? new Zona(
                doc.getInteger("id_zona"),
                doc.getString("nombre"),
                doc.getString("provincia"),
                doc.getString("pais"),
                doc.getString("descripcion")
        ) : null;
    }

    public void updateZona(int idZona, String nuevaDescripcion) {
        // MongoDB
        MongoCollection<Document> collection = mongoDB.getCollection("zonas");
        collection.updateOne(Filters.eq("id_zona", idZona), Updates.set("descripcion", nuevaDescripcion));

        // Neo4j
        try (Session session = neo4jDB.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (z:zona {id_zona: $idZona}) " +
                                "SET z.descripcion = $descripcion",
                        Map.of(
                                "idZona", idZona,
                                "descripcion", nuevaDescripcion
                        ));
                return null;
            });
            System.out.println("Zona actualizada en Neo4j con idZona: " + idZona);
        } catch (Exception e) {
            System.err.println("Error al actualizar zona en Neo4j: " + e.getMessage());
        }
    }

  


public void deleteZona(int idZona) {
    MongoCollection<Document> collection = mongoDB.getCollection("zonas");
    collection.deleteOne(Filters.eq("id_zona", idZona));
}
public List<Zona> getZonasDisponibles() {
    List<Zona> zonas = new ArrayList<>();

    try {
        // Conectar a la base de datos y obtener la colección de zonas
        MongoCollection<Document> collection = mongoDB.getCollection("zonas");

        // Obtener todos los documentos y convertirlos a objetos Zona
        for (Document doc : collection.find()) {
            Zona zona = new Zona(
                doc.getInteger("id_zona"),
                doc.getString("nombre"),
                doc.getString("provincia"),
                doc.getString("pais"),
                doc.getString("descripcion")
            );
            zonas.add(zona);
        }

    } catch (Exception e) {
        System.err.println("Error al obtener las zonas: " + e.getMessage());
    }

    return zonas;
}




//--------------------------------------------------------------------------------------------
//Persistencia
    public int getUltimoIdHotel() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_hotel"))
                                .projection(new Document("_id", 0).append("seq", 1))
                                .first();
        return resultado.getInteger("seq");
    }

    public void aumentarUltimoIdHotel() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_hotel"), new Document("$inc", new Document("seq", 1)));
    }

    public int getUltimoIdAmenity() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_amenity"))
                                    .projection(new Document("_id", 0).append("seq", 1))
                                    .first();
        return resultado.getInteger("seq");
    }
    
    public int getUltimoIdHuesped() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_huesped"))
                                    .projection(new Document("_id", 0).append("seq", 1))
                                    .first();
        return resultado.getInteger("seq");
    }
    
    public void aumentarUltimoIdHuesped() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_huesped"), new Document("$inc", new Document("seq", 1)));
    }

    public void aumentarUltimoIdAmenity() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_amenity"), new Document("$inc", new Document("seq", 1)));
    }

    public int getUltimoIdPuntoDeInteres() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_poi"))
                .projection(new Document("_id", 0).append("seq", 1))
                .first();
        return resultado.getInteger("seq");
    }

    

    public void aumentarUltimoIdPuntoDeInteres() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_poi"), new Document("$inc", new Document("seq", 1)));
    }


    public int getUltimoCodReserva() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "cod_reserva"))
                                    .projection(new Document("_id", 0).append("seq", 1))
                                    .first();
        return resultado.getInteger("seq");
    }
    
    public void aumentarUltimoCodReserva() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "cod_reserva"), new Document("$inc", new Document("seq", 1)));
    }

    public int getUltimoIDHabitacion() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_habitacion"))
                .projection(new Document("_id", 0).append("seq", 1))
                .first();
        return resultado.getInteger("seq");
    }

    public void aumentarUltimoIDHabitacion() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_habitacion"), new Document("$inc", new Document("seq", 1)));
    }

    public int getUltimoIDZona() {
        MongoCollection<Document> collection = mongoDB.getCollection("contadores");
        Document resultado = collection.find(new Document("_id", "id_zona"))
                .projection(new Document("_id", 0).append("seq", 1))
                .first();
        return resultado.getInteger("seq");
    }

    public void aumentarUltimoIDZona() {
        mongoDB.getCollection("contadores").updateOne(new Document("_id", "id_zona"), new Document("$inc", new Document("seq", 1)));
    }

    
    












}

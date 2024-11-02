package org.example.controlador;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
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
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Values;

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


    public void updateHotel(int idHotel, String nuevoNombre, String nuevoTelefono, Map<String, String> nuevaDireccion, String nuevoEmail, int nuevaZona) {

        // actualizar el hotel en MongoDB

        MongoCollection<Document> collection = mongoDB.getCollection("hoteles");

        collection.updateOne(
                Filters.eq("id_hotel", idHotel),
                Updates.combine(
                        Updates.set("nombre", nuevoNombre),
                        Updates.set("telefono", nuevoTelefono),
                        Updates.set("direccion", nuevaDireccion),
                        Updates.set("email", nuevoEmail),
                        Updates.set("zona", nuevaZona)
                )
        );

        // Neo4j: Actualizar la relación entre la zona y el hotel
        try (Session session = neo4jDB.session()) {
            // Primero, elimina la relación existente del hotel con la zona anterior
            session.writeTransaction(tx -> {
                tx.run(
                        "MATCH (h:hotel {id_hotel: $id_hotel})-[r:PERTENECE]->(z:zona) " +
                                "DELETE r",
                        Map.of("id_hotel", idHotel)
                );
                return null;
            });

            // Luego, crea la nueva relación con la nueva zona
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


//------------------------------------------------------------------------------------------------------------------------------------
    // CRUD para la entidad Habitacion neo4jDB mongoDB
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

//----------------------------------------------------------------------------------------------------

// CRUD para la entidad Zona

public void createZona(Zona zona) {
    MongoCollection<Document> collection = mongoDB.getCollection("zonas");
    Document document = new Document("id_zona", zona.getIdZona())
            .append("nombre", zona.getNombre())
            .append("provincia", zona.getProvincia())
            .append("pais", zona.getPais())
            .append("descripcion", zona.getDescripcion());
    collection.insertOne(document);
}

public Zona readZona(int idZona) {
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
    MongoCollection<Document> collection = mongoDB.getCollection("zonas");
    collection.updateOne(Filters.eq("id_zona", idZona), Updates.set("descripcion", nuevaDescripcion));
}

public void deleteZona(int idZona) {
    MongoCollection<Document> collection = mongoDB.getCollection("zonas");
    collection.deleteOne(Filters.eq("id_zona", idZona));
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

}

package org.example.controlador;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.conexionmongo.MongoDBConnection;
import org.example.entidades.*;

import java.util.List;
import java.util.Map;

public class CRUDController {

    private final MongoDatabase database;

    public CRUDController() {
        this.database = MongoDBConnection.getDatabase();
    }

    // CRUD para la entidad Hotel
    public void createHotel(Hotel hotel) {
        MongoCollection<Document> collection = database.getCollection("Hoteles");
        Document document = new Document("_id", hotel.getIdHotel())
                .append("nombre", hotel.getNombre())
                .append("telefono", hotel.getTelefono())
                .append("email", hotel.getEmail())
                .append("direccion", hotel.getDireccion())
                .append("habitaciones", hotel.getHabitaciones())
                .append("zona", hotel.getZona());
        collection.insertOne(document);
    }

    public Hotel readHotel(ObjectId idHotel) {
        MongoCollection<Document> collection = database.getCollection("Hoteles");
        Document doc = collection.find(Filters.eq("_id", idHotel)).first();
        return (doc != null) ? new Hotel(
                doc.getObjectId("_id"),
                doc.getString("nombre"),
                doc.getString("telefono"),
                doc.getString("email"),
                (Map<String, String>) doc.get("direccion"),
                (List<Integer>) doc.get("habitaciones"),
                doc.getInteger("zona")
        ) : null;
    }

    public void updateHotel(ObjectId idHotel, String nuevoNombre) {
        MongoCollection<Document> collection = database.getCollection("Hoteles");
        collection.updateOne(Filters.eq("_id", idHotel), Updates.set("nombre", nuevoNombre));
    }

    public void deleteHotel(ObjectId idHotel) {
        MongoCollection<Document> collection = database.getCollection("Hoteles");
        collection.deleteOne(Filters.eq("_id", idHotel));
    }

    // CRUD para la entidad Habitacion
    public void createHabitacion(Habitacion habitacion) {
        MongoCollection<Document> collection = database.getCollection("Habitaciones");
        Document document = new Document("nroHabitacion", habitacion.getNroHabitacion())
                .append("idHotel", habitacion.getIdHotel())
                .append("tipoHabitacion", habitacion.getTipoHabitacion())
                .append("amenities", habitacion.getAmenities());
        collection.insertOne(document);
    }

    public Habitacion readHabitacion(int nroHabitacion) {
        MongoCollection<Document> collection = database.getCollection("Habitaciones");
        Document doc = collection.find(Filters.eq("nroHabitacion", nroHabitacion)).first();
        return (doc != null) ? new Habitacion(
                doc.getInteger("nroHabitacion"),
                doc.getObjectId("idHotel"),
                doc.getString("tipoHabitacion"),
                (List<Integer>) doc.get("amenities")
        ) : null;
    }

    public void updateHabitacion(int nroHabitacion, String nuevoTipoHabitacion) {
        MongoCollection<Document> collection = database.getCollection("Habitaciones");
        collection.updateOne(Filters.eq("nroHabitacion", nroHabitacion), Updates.set("tipoHabitacion", nuevoTipoHabitacion));
    }

    public void deleteHabitacion(int nroHabitacion) {
        MongoCollection<Document> collection = database.getCollection("Habitaciones");
        collection.deleteOne(Filters.eq("nroHabitacion", nroHabitacion));
    }

    // CRUD para la entidad Huesped
    public void createHuesped(Huesped huesped) {
        MongoCollection<Document> collection = database.getCollection("Huespedes");
        Document document = new Document("_id", huesped.getIdHuesped())
                .append("nombre", huesped.getNombre())
                .append("apellido", huesped.getApellido())
                .append("telefono", huesped.getTelefono())
                .append("email", huesped.getEmail())
                .append("direccion", huesped.getDireccion());
        collection.insertOne(document);
    }

    public Huesped readHuesped(ObjectId idHuesped) {
        MongoCollection<Document> collection = database.getCollection("Huespedes");
        Document doc = collection.find(Filters.eq("_id", idHuesped)).first();
        return (doc != null) ? new Huesped(
                doc.getObjectId("_id"),
                doc.getString("nombre"),
                doc.getString("apellido"),
                doc.getString("telefono"),
                doc.getString("email"),
                (Map<String, String>) doc.get("direccion")
        ) : null;
    }

    public void updateHuesped(ObjectId idHuesped, String nuevoEmail) {
        MongoCollection<Document> collection = database.getCollection("Huespedes");
        collection.updateOne(Filters.eq("_id", idHuesped), Updates.set("email", nuevoEmail));
    }

    public void deleteHuesped(ObjectId idHuesped) {
        MongoCollection<Document> collection = database.getCollection("Huespedes");
        collection.deleteOne(Filters.eq("_id", idHuesped));
    }

    // CRUD para la entidad Reserva
    public void createReserva(Reserva reserva) {
        MongoCollection<Document> collection = database.getCollection("Reservas");
        Document document = new Document("codReserva", reserva.getCodReserva())
                .append("checkin", reserva.getCheckin())
                .append("checkout", reserva.getCheckout())
                .append("estadoReserva", reserva.getEstadoReserva())
                .append("tarifa", reserva.getTarifa())
                .append("idHotel", reserva.getIdHotel())
                .append("idHabitacion", reserva.getIdHabitacion())
                .append("idHuesped", reserva.getIdHuesped());
        collection.insertOne(document);
    }

    public Reserva readReserva(int codReserva) {
        MongoCollection<Document> collection = database.getCollection("Reservas");
        Document doc = collection.find(Filters.eq("codReserva", codReserva)).first();
        return (doc != null) ? new Reserva(
                doc.getInteger("codReserva"),
                doc.getString("checkin"),
                doc.getString("checkout"),
                doc.getString("estadoReserva"),
                doc.getDouble("tarifa"),
                doc.getObjectId("idHotel"),
                doc.getInteger("idHabitacion"),
                doc.getObjectId("idHuesped")
        ) : null;
    }

    public void updateReserva(int codReserva, String nuevoEstado) {
        MongoCollection<Document> collection = database.getCollection("Reservas");
        collection.updateOne(Filters.eq("codReserva", codReserva), Updates.set("estadoReserva", nuevoEstado));
    }

    public void deleteReserva(int codReserva) {
        MongoCollection<Document> collection = database.getCollection("Reservas");
        collection.deleteOne(Filters.eq("codReserva", codReserva));
    }

    // CRUD para la entidad Amenity
public void createAmenity(Amenity amenity) {
    MongoCollection<Document> collection = database.getCollection("Amenities");
    Document document = new Document("idAmenity", amenity.getIdAmenity())
            .append("nombre", amenity.getNombre())
            .append("descripcion", amenity.getDescripcion());
    collection.insertOne(document);
}

public Amenity readAmenity(int idAmenity) {
    MongoCollection<Document> collection = database.getCollection("Amenities");
    Document doc = collection.find(Filters.eq("idAmenity", idAmenity)).first();
    return (doc != null) ? new Amenity(
            doc.getInteger("idAmenity"),
            doc.getString("nombre"),
            doc.getString("descripcion")
    ) : null;
}

public void updateAmenity(int idAmenity, String nuevaDescripcion) {
    MongoCollection<Document> collection = database.getCollection("Amenities");
    collection.updateOne(Filters.eq("idAmenity", idAmenity), Updates.set("descripcion", nuevaDescripcion));
}

public void deleteAmenity(int idAmenity) {
    MongoCollection<Document> collection = database.getCollection("Amenities");
    collection.deleteOne(Filters.eq("idAmenity", idAmenity));
}

// CRUD para la entidad Zona
public void createZona(Zona zona) {
    MongoCollection<Document> collection = database.getCollection("Zonas");
    Document document = new Document("idZona", zona.getIdZona())
            .append("nombre", zona.getNombre())
            .append("provincia", zona.getProvincia())
            .append("pais", zona.getPais())
            .append("descripcion", zona.getDescripcion());
    collection.insertOne(document);
}

public Zona readZona(int idZona) {
    MongoCollection<Document> collection = database.getCollection("Zonas");
    Document doc = collection.find(Filters.eq("idZona", idZona)).first();
    return (doc != null) ? new Zona(
            doc.getInteger("idZona"),
            doc.getString("nombre"),
            doc.getString("provincia"),
            doc.getString("pais"),
            doc.getString("descripcion")
    ) : null;
}

public void updateZona(int idZona, String nuevaDescripcion) {
    MongoCollection<Document> collection = database.getCollection("Zonas");
    collection.updateOne(Filters.eq("idZona", idZona), Updates.set("descripcion", nuevaDescripcion));
}

public void deleteZona(int idZona) {
    MongoCollection<Document> collection = database.getCollection("Zonas");
    collection.deleteOne(Filters.eq("idZona", idZona));
}

// CRUD para la entidad PuntoDeInteres
public void createPuntoDeInteres(PuntoDeInteres poi) {
    MongoCollection<Document> collection = database.getCollection("PuntosDeInteres");
    Document document = new Document("idPoi", poi.getIdPoi())
            .append("nombre", poi.getNombre())
            .append("descripcion", poi.getDescripcion())
            .append("zona", poi.getZona());
    collection.insertOne(document);
}

public PuntoDeInteres readPuntoDeInteres(int idPoi) {
    MongoCollection<Document> collection = database.getCollection("PuntosDeInteres");
    Document doc = collection.find(Filters.eq("idPoi", idPoi)).first();
    return (doc != null) ? new PuntoDeInteres(
            doc.getInteger("idPoi"),
            doc.getString("nombre"),
            doc.getString("descripcion"),
            doc.getInteger("zona")
    ) : null;
}

public void updatePuntoDeInteres(int idPoi, String nuevaDescripcion) {
    MongoCollection<Document> collection = database.getCollection("PuntosDeInteres");
    collection.updateOne(Filters.eq("idPoi", idPoi), Updates.set("descripcion", nuevaDescripcion));
}

public void deletePuntoDeInteres(int idPoi) {
    MongoCollection<Document> collection = database.getCollection("PuntosDeInteres");
    collection.deleteOne(Filters.eq("idPoi", idPoi));
}

}

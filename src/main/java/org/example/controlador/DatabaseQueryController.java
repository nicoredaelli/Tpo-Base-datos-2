package org.example.controlador;

import com.mongodb.client.MongoDatabase;
import org.example.entidades.PuntoDeInteres;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.List;
import java.util.Map;

public class DatabaseQueryController {
    private final MongoDatabase mongoDB;
    private final Driver neo4jDB;

    public DatabaseQueryController(MongoDatabase mongoDB, Driver neo4jDB) {
        this.mongoDB = mongoDB;
        this.neo4jDB = neo4jDB;
    }

    public List<PuntoDeInteres> getPOIsByIDHotel(int idHotel){
        try (Session session = neo4jDB.session()) {
            Result result = session.run(
                    "MATCH (p:poi {id_poi: $idPoi})-[:PERTENECE]->(z:zona)<-[:PERTENECE]-(h:hotel) " +
                            "RETURN h.id_hotel AS id_hotel, h.nombre AS nombre_hotel",
                    Map.of("idPoi", idPoi)
            );

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println("Hotel ID: " + record.get("id_hotel").asInt() +
                        ", Nombre: " + record.get("nombre_hotel").asString());
            }
        }
        return null; //todo
    }

}

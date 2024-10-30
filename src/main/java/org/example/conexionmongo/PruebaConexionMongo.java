package org.example.conexionmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PruebaConexionMongo {

    public static void main(String[] args) {

        String uri = "mongodb+srv://NicolasIglesias:Nico2024@cluster0.o3iom.mongodb.net/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Acceder a la base de datos
            MongoDatabase database = mongoClient.getDatabase("Cadena");

            // Acceder a una colección específica
            MongoCollection<Document> collection = database.getCollection("Hotel");

            // Realizar una operación simple para verificar la conexión (obtener el primer documento)
            Document document = collection.find().first();
            System.out.println("Documento encontrado: " + document);

        } catch (Exception e) {
            System.err.println("Error conectando a MongoDB: " + e.getMessage());
        }
    }
}

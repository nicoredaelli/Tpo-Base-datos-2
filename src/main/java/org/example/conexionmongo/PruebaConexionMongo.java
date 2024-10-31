package org.example.conexionmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class PruebaConexionMongo {
    public static void main(String[] args) {
        try {
            // Obtener la base de datos desde MongoDBConnection
            MongoDatabase database = MongoDBConnection.getDatabase();

            // Listar las colecciones disponibles en la base de datos
            System.out.println("Colecciones en la base de datos '" + database.getName() + "':");
            for (String name : database.listCollectionNames()) {
                System.out.println("- " + name);
            }

        } catch (Exception e) {
            System.err.println("Error al conectarse a MongoDB Atlas: " + e.getMessage());
        }
    }
}


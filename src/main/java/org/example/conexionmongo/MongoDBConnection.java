package org.example.conexionmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    //private static final String URI = "mongodb://localhost:27017";
    private static final String URI = "mongodb+srv://NicolasIglesias:Nico2024@cluster0.o3iom.mongodb.net/tpo-bbdd2?retryWrites=true&w=majority";
    private static final String DATABASE_NAME = "Cadena";
    private static MongoClient mongoClient;

    // metodo para obtener la conexión a la base de datos
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    // Metodo para cerrar la conexión de MongoDB
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}

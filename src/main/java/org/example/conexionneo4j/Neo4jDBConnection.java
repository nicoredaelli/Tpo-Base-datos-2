package org.example.conexionneo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDBConnection {

    private static final String URI = "neo4j+s://aed5b791.databases.neo4j.io"; // Cambia esto por tu URI
    private static final String USERNAME = "neo4j"; // Cambia esto si tienes un usuario diferente
    private static final String PASSWORD = "5ST-FtQ1MLMl6UlkHncU_nqvj_-PHYmehWUCgzhFGGg"; // Reemplaza con tu contraseña

    private static Driver driver;

    // Constructor privado para Singleton
    private Neo4jDBConnection() { }

    // Metodo para obtener la conexión (Singleton)
    public static Driver getConnection() {
        if (driver == null) {
            driver = GraphDatabase.driver(URI, AuthTokens.basic(USERNAME, PASSWORD));
        }
        return driver;
    }

    // Metodo para cerrar la conexión
    public static void closeConnection() {
        if (driver != null) {
            driver.close();
        }
    }
}

package org.example.conexionneo4j;

import org.neo4j.driver.*;

public class PruebaConexionNeo4j {

    public static void main(String[] args) {
        // URI, usuario y contraseña de Neo4j Aura
        String uri = "neo4j+s://aed5b791.databases.neo4j.io";
        String user = "neo4j";
        String password = "5ST-FtQ1MLMl6UlkHncU_nqvj_-PHYmehWUCgzhFGGg";

        // Crear el controlador y la conexión
        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
             Session session = driver.session()) {

            // Ejecutar una consulta simple para verificar la conexión
            Result result = session.run("RETURN 'Conexión exitosa a Neo4j Aura' AS message");
            System.out.println(result.single().get("message").asString());

        } catch (Exception e) {
            System.err.println("Error conectando a Neo4j Aura: " + e.getMessage());
        }
    }
}
